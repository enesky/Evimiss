package com.enesky.evimiss.utils

import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import android.util.Log
import com.enesky.evimiss.ui.theme.secondary
import java.util.*

/**
 * Created by Enes Kamil YILMAZ on 18/09/2021
 */

object CalendarUtil {

    private val EVENT_PROJECTION: Array<String> = arrayOf(
        CalendarContract.Calendars._ID,                     // 0
        CalendarContract.Calendars.ACCOUNT_NAME,            // 1
        CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,   // 2
        CalendarContract.Calendars.OWNER_ACCOUNT            // 3
    )

    private const val PROJECTION_ID_INDEX: Int = 0
    private const val PROJECTION_ACCOUNT_NAME_INDEX: Int = 1
    private const val PROJECTION_DISPLAY_NAME_INDEX: Int = 2
    private const val PROJECTION_OWNER_ACCOUNT_INDEX: Int = 3

    fun getCalendarEvents(
        contentResolver: ContentResolver
    ) {
        val cur: Cursor? = contentResolver.query(
            Uri.parse("content://com.android.calendar/events"),
            arrayOf("calendar_id", "title", "description", "dtstart", "dtend", "eventLocation"),
            null, null, null);

        while (cur?.moveToNext() == true) {
            val calendarId = cur.getString(0)
            val title = cur.getString(1)
            val description = cur.getString(2)
            val dtstart = cur.getString(3)
            val dtend = cur.getString(4)
            val eventLocation = cur.getString(5)

            Log.d("CalendarUtil", "@@@@ calendarId: $calendarId, title: $title," +
                    " description: $description, dtstart: $dtstart, dtend: $dtend, eventLocation: $eventLocation @@@@")
        }
    }

    fun addEvent(
        contentResolver: ContentResolver,
        title: String,
        description: String? = "",
        startMillis: Long,
        endMillis: Long
    ) {
        val calendarID: Long = 3

        val values = ContentValues().apply {
            CalendarContract.Events.CALENDAR_ID to calendarID
            CalendarContract.Events.DTSTART to startMillis
            CalendarContract.Events.DTEND to endMillis
            CalendarContract.Events.EVENT_TIMEZONE to TimeZone.getDefault().displayName
            CalendarContract.Events.TITLE to title
            CalendarContract.Events.DESCRIPTION to description
            CalendarContract.Events.CALENDAR_COLOR_KEY to secondary
        }
        contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
    }

}