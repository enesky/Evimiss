package com.enesky.evimiss.utils

import android.app.Activity
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import android.util.Log
import java.util.*

/**
 * Created by Enes Kamil YILMAZ on 18/09/2021
 */

object CalendarUtil {

    // Projection array. Creating indices for this array instead of doing
// dynamic lookups improves performance.
    private val EVENT_PROJECTION: Array<String> = arrayOf(
        CalendarContract.Calendars._ID,                     // 0
        CalendarContract.Calendars.ACCOUNT_NAME,            // 1
        CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,   // 2
        CalendarContract.Calendars.OWNER_ACCOUNT            // 3
    )

    // The indices for the projection array above.
    private const val PROJECTION_ID_INDEX: Int = 0
    private const val PROJECTION_ACCOUNT_NAME_INDEX: Int = 1
    private const val PROJECTION_DISPLAY_NAME_INDEX: Int = 2
    private const val PROJECTION_OWNER_ACCOUNT_INDEX: Int = 3

    fun Activity.getCalendarAddEvent() {
        // Run query
        val uri: Uri = CalendarContract.Calendars.CONTENT_URI
        val selection: String =
            "((${CalendarContract.Calendars.ACCOUNT_NAME} = ?) AND (" +
                "${CalendarContract.Calendars.ACCOUNT_TYPE} = ?) AND (" +
                "${CalendarContract.Calendars.OWNER_ACCOUNT} = ?))"
        val selectionArgs: Array<String> = arrayOf("eneskamilyilmaz@gmail.com", CalendarContract.ACCOUNT_TYPE_LOCAL, "eneskamilyilmaz@gmail.com")
        val cur: Cursor? = contentResolver.query(uri, EVENT_PROJECTION, selection, selectionArgs, null)

        // Use the cursor to step through the returned records
        while (cur?.moveToNext() == true) {
            // Get the field values
            val calID: Long = cur.getLong(PROJECTION_ID_INDEX)
            val displayName: String = cur.getString(PROJECTION_DISPLAY_NAME_INDEX)
            val accountName: String = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX)
            val ownerName: String = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX)
            // Do something with the values...
            Log.d("CalendarUtil", "@@@@ calId: $calID, displayName: $displayName, accountName: $accountName, ownerName: $ownerName,")
        }

        addEvent()
    }


    fun Activity.addEvent() {
        val calID: Long = 3
        val startMillis: Long = Calendar.getInstance().run {
            set(2021, 8, 19, 11, 30)
            timeInMillis
        }
        val endMillis: Long = Calendar.getInstance().run {
            set(2021, 8, 19, 12, 45)
            timeInMillis
        }

        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, startMillis)
            put(CalendarContract.Events.DTEND, endMillis)
            put(CalendarContract.Events.TITLE, "Jazzercise")
            put(CalendarContract.Events.DESCRIPTION, "Group workout")
            put(CalendarContract.Events.CALENDAR_ID, calID)
            put(CalendarContract.Events.EVENT_TIMEZONE, "America/Los_Angeles")
        }
        contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
    }
}