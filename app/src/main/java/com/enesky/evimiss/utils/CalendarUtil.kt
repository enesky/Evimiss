package com.enesky.evimiss.utils

import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import android.provider.CalendarContract
import android.util.Log
import com.enesky.evimiss.data.CalendarEntity
import com.enesky.evimiss.data.EventEntity
import com.enesky.evimiss.ui.theme.secondary
import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import java.util.*
import javax.inject.Singleton

/**
 * Created by Enes Kamil YILMAZ on 18/09/2021
 */

@Singleton
object CalendarUtil {

    private val CALENDAR_CONTENT_URI = CalendarContract.Calendars.CONTENT_URI
    private val CALENDAR_PROJECTION = arrayOf(
        CalendarContract.Calendars._ID,            //0
        CalendarContract.Calendars.ACCOUNT_NAME,   //1
        CalendarContract.Calendars.NAME,
        CalendarContract.Calendars.OWNER_ACCOUNT,
    )
    private val calendarList = mutableListOf<CalendarEntity>()
    private const val CALENDARS_SORT_ORDER = "${CalendarContract.Calendars._ID} ASC"

    private val EVENT_CONTENT_URI = CalendarContract.Events.CONTENT_URI
    private val EVENT_PROJECTION = arrayOf(
        CalendarContract.Events.CALENDAR_ID,      //0
        CalendarContract.Events.TITLE,            //1
        CalendarContract.Events.DESCRIPTION,      //2
        CalendarContract.Events.DTSTART,          //3
        CalendarContract.Events.DTEND,            //4
        CalendarContract.Events.EVENT_LOCATION,   //5
        CalendarContract.Events.ALL_DAY,          //6
        CalendarContract.Events.RRULE,
        CalendarContract.Events.DURATION
    )
    private var EVENT_SELECTION = ""
    private var EVENT_SELECTION_WITH_DATE = "(${CalendarContract.Events.DTSTART} >= ?" +
            " AND (${CalendarContract.Events.DTEND} <= ?" +
            " OR ${CalendarContract.Events.DTEND} IS NULL))"
    private var calendarIdQuery = "${CalendarContract.Events.CALENDAR_ID} IN (?"
    private val EVENT_SELECTION_ARGS = mutableListOf<String>()
    private const val EVENTS_SORT_ORDER = "${CalendarContract.Events.DTSTART} ASC"

    fun getCalendars(contentResolver: ContentResolver?) {
        val calendarCursor: Cursor? = contentResolver?.query(
            CALENDAR_CONTENT_URI, CALENDAR_PROJECTION, null,
            null, CALENDARS_SORT_ORDER
        )
        while (calendarCursor?.moveToNext() == true) {
            val id = calendarCursor.getString(0)
            val accountName = calendarCursor.getString(1)
            val name = calendarCursor.getString(2)
            val ownerAccount = calendarCursor.getString(3)

            //Get users calendar id numbers
            if (accountName == getUserEmail() || isAnonymous() == true) { //TODO: Add partner emails here
                val calendar = CalendarEntity(id = id, isMain = ownerAccount == name)
                calendarList.add(calendar)
                if (calendar.isMain)
                    SharedPrefUtil.mainCalendarEntity = calendar
            }
        }

        if (calendarList.isEmpty()) //If no calendar exist shut it down
            return
        else
            SharedPrefUtil.mainCalendarList = calendarList

        //Add CalendarIDs to EVENT_SELECTION_ARGS
        for (calendar in calendarList)
            EVENT_SELECTION_ARGS.add(0, calendar.id)

        //Update Selection if more than 1 calendar available
        if (calendarList.size > 1) {
            for (calendar in 1 until calendarList.size)
                calendarIdQuery += ",?"
        }
        calendarIdQuery += ')'
        EVENT_SELECTION = calendarIdQuery
    }

    fun getAllCalendarEvents(contentResolver: ContentResolver): MutableList<EventEntity> {
        val eventList: MutableList<EventEntity> = mutableListOf()
        val eventCursor: Cursor? = try {
            contentResolver.query(
            EVENT_CONTENT_URI, EVENT_PROJECTION, null,
            null, null
            )
        } catch (e: Exception) {
            Log.d("CalendarUtil", "getAllCalendarEvents: " + e.localizedMessage.toString())
            FirebaseCrashlytics.getInstance().recordException(e)
            null
        }
        while (eventCursor?.moveToNext() == true) {
            val eventEntity = eventEntityItem(eventCursor)
            if (eventCursor.getString(0) == SharedPrefUtil.mainCalendarEntity.id)
                eventList.add(eventEntity)
            Log.d("CalendarUtil -EventList", eventList.toString())
        }
        return eventList
    }

    fun getCalendarEventsInGivenDatesMonth(
        contentResolver: ContentResolver?,
        givenDate: LocalDate? = null
    ): MutableList<EventEntity> {
        val startEndMillis = getMonthsStartEndMillis(givenDate)
        val eventSelectionArgs = mutableListOf(startEndMillis[0], startEndMillis[1]).apply {
            addAll(0, EVENT_SELECTION_ARGS)
        }
        if (EVENT_SELECTION_WITH_DATE.contains(calendarIdQuery).not())
            EVENT_SELECTION_WITH_DATE = "$calendarIdQuery AND $EVENT_SELECTION_WITH_DATE"

        val eventCursor: Cursor? = try {
            contentResolver?.query(
                EVENT_CONTENT_URI, EVENT_PROJECTION, EVENT_SELECTION_WITH_DATE,
                eventSelectionArgs.toTypedArray(), EVENTS_SORT_ORDER
            )
        } catch (e: Exception) {
            Log.d("CalendarUtil", "getCalendarEventsInGivenDatesMonth: " + e.localizedMessage.toString())
            FirebaseCrashlytics.getInstance().recordException(e)
            null
        }

        val eventList: MutableList<EventEntity> = mutableListOf()
        while (eventCursor?.moveToNext() == true)
            eventList.add(eventEntityItem(eventCursor))
        return eventList
    }

    fun getCalendarEventsInGivenDate(
        contentResolver: ContentResolver?,
        givenDate: LocalDate? = null
    ): MutableList<EventEntity> {
        val startEndMillis = getStartEndMillis(givenDate)
        val eventSelectionArgs = mutableListOf(startEndMillis[0], startEndMillis[1]).apply {
            addAll(0, EVENT_SELECTION_ARGS)
        }
        if (EVENT_SELECTION_WITH_DATE.contains(calendarIdQuery).not())
            EVENT_SELECTION_WITH_DATE = "$calendarIdQuery AND $EVENT_SELECTION_WITH_DATE"

        val eventCursor: Cursor? = try {
            contentResolver?.query(
                EVENT_CONTENT_URI, EVENT_PROJECTION, EVENT_SELECTION_WITH_DATE,
                eventSelectionArgs.toTypedArray(), EVENTS_SORT_ORDER
            )
        } catch (e: Exception) {
            Log.d("CalendarUtil","getCalendarEventsInGivenDate: " + e.localizedMessage.toString())
            FirebaseCrashlytics.getInstance().recordException(e)
            null
        }

        val eventList: MutableList<EventEntity> = mutableListOf()
        while (eventCursor?.moveToNext() == true)
            eventList.add(eventEntityItem(eventCursor))

        return eventList
    }

    fun addEvent(
        contentResolver: ContentResolver,
        title: String,
        description: String? = "",
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ) {
        val values = ContentValues().apply {
            CalendarContract.Events.CALENDAR_ID to SharedPrefUtil.mainCalendarEntity.id
            CalendarContract.Events.TITLE to title
            CalendarContract.Events.DESCRIPTION to description
            CalendarContract.Events.DTSTART to startDateTime.getMillis()
            CalendarContract.Events.DTEND to endDateTime.getMillis()
            CalendarContract.Events.EVENT_TIMEZONE to TimeZone.getDefault().displayName
            CalendarContract.Events.CALENDAR_COLOR_KEY to secondary
        }
        val uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)

        // get the event ID that is the last element in the Uri
        //val eventID = uri?.lastPathSegment
    }

    private fun eventEntityItem(eventCursor: Cursor) = EventEntity(
        id = eventCursor.position.toString(), //TODO: get event id
        calendarId = eventCursor.getString(0),
        title = eventCursor.getString(1),
        description = eventCursor.getString(2),
        dtStart = eventCursor.getString(3),
        dtEnd = eventCursor.getString(4),
        location = eventCursor.getString(5),
        isAllDay = eventCursor.getString(6) != "0",
        rRule = eventCursor.getString(7),
        duration = eventCursor.getString(8)
    )

}