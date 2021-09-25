package com.enesky.evimiss.utils

import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import android.provider.CalendarContract
import android.util.Log
import com.enesky.evimiss.ui.theme.secondary
import org.threeten.bp.LocalDateTime
import java.util.*

/**
 * Created by Enes Kamil YILMAZ on 18/09/2021
 */

object CalendarUtil {

    private val CALENDAR_CONTENT_URI = CalendarContract.Calendars.CONTENT_URI
    private val CALENDAR_PROJECTION = arrayOf(
        CalendarContract.Calendars._ID,            //0
        CalendarContract.Calendars.ACCOUNT_NAME    //1
        //CalendarContract.Calendars.NAME,
        //CalendarContract.Calendars.OWNER_ACCOUNT,
    )
    private val calendarIdList = mutableListOf<String>()

    private val EVENT_CONTENT_URI = CalendarContract.Events.CONTENT_URI
    private val EVENT_PROJECTION = arrayOf(
        CalendarContract.Events.CALENDAR_ID,      //0
        CalendarContract.Events.TITLE,            //1
        CalendarContract.Events.DESCRIPTION,      //2
        CalendarContract.Events.DTSTART,          //3
        CalendarContract.Events.DTEND,            //4
        CalendarContract.Events.EVENT_LOCATION,   //5
        CalendarContract.Events.DELETED,          //6
    )
    private var EVENT_SELECTION = "(" +
            //"(${CalendarContract.Events.DTSTART} + >= ?) AND " +
            //"(${CalendarContract.Events.DTEND}} <= ?) AND " +
            "(${CalendarContract.Events.DELETED} = ?) AND " +
            "(${CalendarContract.Events.CALENDAR_ID} = ?)" +
            ")"

    private val EVENT_SELECTION_ARGS = mutableListOf(
        "0", //Deleted == false
    )

    private const val SORT_ORDER = CalendarContract.EventsEntity.DTSTART + " ASC"

    fun getCalendarEvents(contentResolver: ContentResolver) {
        val calendarCursor: Cursor? = contentResolver.query(
            CALENDAR_CONTENT_URI,
            CALENDAR_PROJECTION,
            null, null, null)
        while (calendarCursor?.moveToNext() == true) {
            val id = calendarCursor.getString(0)
            val accountName = calendarCursor.getString(1)

            //Get users calendar id numbers
            if (accountName == getUserEmail() || isAnonymous() == true) //TODO: Add partner emails here
                calendarIdList.add(id)
        }

        //Add CalendarIDs to EVENT_SELECTION_ARGS
        for (calendar in calendarIdList)
            EVENT_SELECTION_ARGS.add(calendar)

        //Update Selection if more than 1 calendar available
        if (calendarIdList.size > 1) {
            EVENT_SELECTION = EVENT_SELECTION.apply {
                dropLast(1) // Delete character ')'
                for (calendar in 1 until calendarIdList.size)
                    plus(" OR (${CalendarContract.Events.CALENDAR_ID} = ?)")
                plus(')') // Add deleted character ')'
            }
        }

        val eventCursor: Cursor? = contentResolver.query(
            EVENT_CONTENT_URI,
            EVENT_PROJECTION,
            EVENT_SELECTION,
            EVENT_SELECTION_ARGS.toTypedArray(),
            SORT_ORDER
        )

        while (eventCursor?.moveToNext() == true) {
            val calendarId = eventCursor.getString(0)
            val title = eventCursor.getString(1)
            val description = eventCursor.getString(2)
            val dtstart = eventCursor.getLong(3)
            val dtend = eventCursor.getLong(4)
            val location = eventCursor.getString(5)
            val deleted = eventCursor.getString(6)
            Log.d("CalendarUtil - Events",
                "CalendarId: $calendarId, " +
                      "Title: $title, Description: $description, " +
                      "Start Date: ${dtstart.convert2DetailedDateTime()}, " +
                      "End Date: ${dtend.convert2DetailedDateTime()}, " +
                      "Deleted: ${deleted != "0"}, Location: $location")
        }
    }

    fun addEvent(
        contentResolver: ContentResolver,
        calendarID: String,
        title: String,
        description: String? = "",
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ) {
        val values = ContentValues().apply {
            CalendarContract.Events.CALENDAR_ID to calendarID
            CalendarContract.Events.TITLE to title
            CalendarContract.Events.DESCRIPTION to description
            CalendarContract.Events.DTSTART to startDateTime.getMillis()
            CalendarContract.Events.DTEND to endDateTime.getMillis()
            CalendarContract.Events.EVENT_TIMEZONE to TimeZone.getDefault().displayName
            CalendarContract.Events.CALENDAR_COLOR_KEY to secondary
        }
        contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
    }

}