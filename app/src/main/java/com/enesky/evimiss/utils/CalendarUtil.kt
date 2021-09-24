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
        val uri: Uri = CalendarContract.Calendars.CONTENT_URI
        val selection: String =
            "((${CalendarContract.Calendars.ACCOUNT_NAME} = ?) AND " +
            "(${CalendarContract.Calendars.ACCOUNT_TYPE} = ?) AND " +
            "(${CalendarContract.Calendars.OWNER_ACCOUNT} = ?))"
        val selectionArgs: Array<String> = arrayOf(
            "eneskamilyilmaz@gmail.com",
            CalendarContract.ACCOUNT_TYPE_LOCAL,
            "eneskamilyilmaz@gmail.com"
        )
        val cur: Cursor? = contentResolver.query(uri, EVENT_PROJECTION, selection, selectionArgs, null)

        while (cur?.moveToNext() == true) {
            val calID: Long = cur.getLong(PROJECTION_ID_INDEX)
            val displayName: String = cur.getString(PROJECTION_DISPLAY_NAME_INDEX)
            val accountName: String = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX)
            val ownerName: String = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX)

            Log.d("CalendarUtil", "@@@@ calId: $calID, displayName: $displayName, accountName: $accountName, ownerName: $ownerName,")
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