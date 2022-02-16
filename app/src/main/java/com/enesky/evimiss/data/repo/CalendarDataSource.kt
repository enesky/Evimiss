package com.enesky.evimiss.data.repo

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract.*
import android.text.format.DateUtils
import android.util.Log
import com.enesky.evimiss.App
import com.enesky.evimiss.data.model.AttendeeEntity
import com.enesky.evimiss.data.model.CalendarEntity
import com.enesky.evimiss.data.model.EventEntity
import com.enesky.evimiss.ui.theme.secondary
import com.enesky.evimiss.utils.*
import dagger.hilt.android.scopes.ActivityScoped
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import java.util.*
import javax.inject.Inject
import kotlin.system.measureTimeMillis

/**
 * Created by Enes Kamil YILMAZ on 18/09/2021
 * This class provides access to the user's information in the base calendar application on their phone.
 */

@ActivityScoped
class CalendarDataSource @Inject constructor(
    private val contentResolver: ContentResolver?
) {

    //Calendar
    private val calendarContentUri = Calendars.CONTENT_URI
    private val calendarList = mutableListOf<CalendarEntity>()
    private val calendarProjection = arrayOf(
        Calendars._ID,            //0
        Calendars.ACCOUNT_NAME,   //1
        Calendars.NAME,           //2
        Calendars.OWNER_ACCOUNT,  //3
    )
    //Instances
    private val instanceProjection = arrayOf(
        Instances.EVENT_ID,              // 1
        Calendars._ID,                   // 2
        Instances.TITLE,                 // 3
        Instances.DESCRIPTION,           // 4
        Instances.EVENT_TIMEZONE,        // 5
        //Instances.DTSTART,               // 6
        //Instances.DTEND,                 // 7
        Instances.BEGIN,                 // 8
        Instances.END,                   // 9
        Instances.ALL_DAY,               // 10
        Instances.RRULE,                 // 11
        Instances.RDATE,                 // 12
        Instances.DURATION,              // 13
        Instances.EVENT_LOCATION,        // 14
        Instances.EVENT_COLOR_KEY,         // 15
        Instances.SELF_ATTENDEE_STATUS,  // 16
        Events.ORGANIZER,                // 17
        Instances.ALL_DAY + "=1 OR (" + Instances.END + "-" + Instances.BEGIN + ")>=" + DateUtils.DAY_IN_MILLIS + " AS dispAllday", // 18
    )
    private val instancesSortOrder = "${Instances.BEGIN} ASC"

    //Attendees
    private val attendeeContentUri = Attendees.CONTENT_URI
    private val attendeeProjection = arrayOf(
        Attendees.EVENT_ID,
        Attendees.ATTENDEE_NAME,
        Attendees.ATTENDEE_EMAIL
    )
    private fun attendeesQuery(eventID: String?) = "${Attendees.EVENT_ID} = $eventID"

    /**
     * Retrieves calendar informations from the base calendar application on the user's device
     * that matches the Google mail logged into the application.
     */
    fun init() {
        val millis = monthlyMillis()
        val builder: Uri.Builder = Instances.CONTENT_URI.buildUpon()
        ContentUris.appendId(builder, millis.first())
        ContentUris.appendId(builder, millis.last())
        contentResolver?.query(
            builder.build(), calendarProjection, null, null, null
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                CalendarEntity(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
                ).apply {
                    calendarList.add(this)
                    if (isMain() && (accountName == getUserEmail() || isAnonymous() == true))
                        SharedPrefUtil.mainCalendarEntity = this
                }
            }
        }
        if (calendarList.isEmpty())
            return
        else
            SharedPrefUtil.mainCalendarList = calendarList
    }

    /**
     * Retrieves given date's or month's events (by CalendarContract.Instances) from the
     * base calendar application on the user's device that matches the Google mail logged
     * into the application.
     */
    fun getEvents(
        givenDate: LocalDate? = null,
        eventsInterval: EventsInterval? = EventsInterval.Quarterly
    ): MutableMap<LocalDate, MutableList<EventEntity>> {
        val millis = when (eventsInterval) {
            EventsInterval.Daily -> dailyMillis(givenDate)
            EventsInterval.Monthly -> monthlyMillis(givenDate)
            else -> quarterlyMillis(givenDate)
        }
        val builder: Uri.Builder = Instances.CONTENT_URI.buildUpon()
        ContentUris.appendId(builder, millis.first())
        ContentUris.appendId(builder, millis.last())
        val internalMillis = millisInternalDates(millis.first(), millis.last())
        val eventMap: MutableMap<LocalDate, MutableList<EventEntity>> = mutableMapOf()
        for (localDate in internalMillis)
            eventMap[localDate] = mutableListOf()
        val eventCursor: Cursor? = try {
            contentResolver?.query(
                builder.build(), instanceProjection, null, null, instancesSortOrder
            )
        } catch (e: Exception) {
            App.mCrashlytics?.recordException(e)
            null
        }

        while (eventCursor?.moveToNext() == true) {
            val eventEntity = eventEntity(eventCursor).apply { attendees = getAttendees(id) }
            val dateList = eventEntity.dateList
            for (localDate in dateList)
                if (eventMap.contains(localDate))
                    eventMap[localDate]?.add(eventEntity)
        }
        eventCursor?.close()
        return eventMap
    }

    /**
     * Retrieves attendee info from base calendar application on the user's device
     */
    private fun getAttendees(eventID: String? = null): MutableList<AttendeeEntity> {
        val cursor: Cursor? = try {
            contentResolver?.query(
                attendeeContentUri, attendeeProjection, attendeesQuery(eventID),
                null, null
            )

        } catch (e: Exception) {
            App.mCrashlytics?.recordException(e)
            null
        }

        val attendeeList: MutableList<AttendeeEntity> = mutableListOf()
        while (cursor?.moveToNext() == true)
            attendeeList.add(attendeeEntity(cursor))
        cursor?.close()
        return attendeeList
    }

    /**
     * Add's events to base calendar application.
     */
    fun addEvent(
        title: String,
        description: String? = "",
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ): String? {
        val values = ContentValues().apply {
            Events.CALENDAR_ID to SharedPrefUtil.mainCalendarEntity.id
            Events.TITLE to title
            Events.DESCRIPTION to description
            Events.DTSTART to startDateTime.getMillis()
            Events.DTEND to endDateTime.getMillis()
            Events.EVENT_TIMEZONE to TimeZone.getDefault().displayName
            Events.CALENDAR_COLOR_KEY to secondary
        }
        val uri = contentResolver?.insert(Events.CONTENT_URI, values)

        // get the event ID that is the last element in the Uri
        return uri?.lastPathSegment
    }

    /**
     * Converts calendar events to EventEntity model.
     */
    private fun eventEntity(eventCursor: Cursor) =
        EventEntity(
            id = eventCursor.getString(0),
            calendarId = eventCursor.getString(1),
            title = eventCursor.getString(2),
            description = eventCursor.getString(3),
            timeZone = eventCursor.getString(4),
            //dtStart = eventCursor.getString(5),
            //dtEnd = eventCursor.getString(6),
            begin = eventCursor.getString(5),
            end = eventCursor.getString(6),
            isAllDay = eventCursor.getString(7) != "0",
            rRule = eventCursor.getString(8),
            rDate = eventCursor.getString(9),
            duration = eventCursor.getString(10),
            location = eventCursor.getString(11),
            displayColor = eventCursor.getString(12),
            selfAttendeeStatus = eventCursor.getString(13),
            organizer = eventCursor.getString(14),
            idk = eventCursor.getString(15) != "0",
        )


    /**
     * Converts calendar attendees to AttendeeEntity model.
     */
    private fun attendeeEntity(eventCursor: Cursor) =
        AttendeeEntity(
            eventID = eventCursor.getString(0),
            name = eventCursor.getString(1),
            email = eventCursor.getString(2),
        )

    private fun eventEntityFromInstance(eventCursor: Cursor) =
        EventEntity(
            id = eventCursor.getString(0),         //Instances.EVENT_ID,
            title = eventCursor.getString(1),      //Instances.TITLE,
            begin = eventCursor.getString(2),    //Instances.DTSTART
            end = eventCursor.getString(3),      //Instances.DTEND
            calendarId = eventCursor.getString(4), //Instances.BEGIN
            location = eventCursor.getString(5),   //Instances.END
            description = eventCursor.getString(6) //Instances.DESCRIPTION
        )

    /*
    private fun forceSync() {
        val extras = Bundle()
        extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true)
        extras.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        val account = Account("example.email@ail.com", "com.google")
        ContentResolver.requestSync(account, CalendarContract.AUTHORITY, extras)
    }

    private fun forceSync2() {
        contentResolver?.update(
            ContentUris.withAppendedId(calendarContentUri, 3),
            ContentValues().apply {
                put(Calendars.SYNC_EVENTS, 1)
                put(Calendars.VISIBLE, 1)
            }, null, null)
    }

    private fun syncCalendars() {
        val authority = CalendarContract.Calendars.CONTENT_URI.authority
        for (calendar in SharedPrefUtil.mainCalendarList)
            ContentResolver.requestSync(calendar, authority, bundleOf(ContentResolver.SYNC_EXTRAS_MANUAL to true))
    }
    */

}