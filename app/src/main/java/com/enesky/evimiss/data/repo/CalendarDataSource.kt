package com.enesky.evimiss.data.repo

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract.*
import android.text.format.DateUtils
import com.enesky.evimiss.App
import com.enesky.evimiss.data.model.AttendeeEntity
import com.enesky.evimiss.data.model.CalendarEntity
import com.enesky.evimiss.data.model.EventEntity
import com.enesky.evimiss.ui.theme.secondary
import com.enesky.evimiss.utils.*
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.scopes.ActivityScoped
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import java.util.*
import javax.inject.Inject

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

    //Will be used for getting the event ids
    private val instanceProjection = arrayOf(
        Instances.EVENT_ID,              // 1
        Calendars._ID,                   // 2
        Instances.TITLE,                 // 3
        Instances.DESCRIPTION,           // 4
        Instances.EVENT_TIMEZONE,        // 5
        Instances.DTSTART,               // 6
        Instances.DTEND,                 // 7
        Instances.BEGIN,                 // 8
        Instances.END,                   // 9
        Instances.ALL_DAY,               // 10
        Instances.RRULE,                 // 11
        Instances.RDATE,                 // 12
        Instances.DURATION,              // 13
        Instances.EVENT_LOCATION,        // 14
        Instances.DISPLAY_COLOR,         // 15
        Instances.SELF_ATTENDEE_STATUS,  // 16
        Events.ORGANIZER,                // 17
        Instances.ALL_DAY + "=1 OR (" + Instances.END + "-" + Instances.BEGIN + ")>=" + DateUtils.DAY_IN_MILLIS + " AS dispAllday", // 18
    )

    //Attendees
    private val attendeeContentUri = Attendees.CONTENT_URI
    private val attendeeProjection = arrayOf(
        Attendees.EVENT_ID,
        Attendees.ATTENDEE_NAME,
        Attendees.ATTENDEE_EMAIL
    )

    private fun attendeesQuery(eventID: String?) = "${Attendees.EVENT_ID} = $eventID"

    //No event id from Events
    private val eventContentUri = Events.CONTENT_URI
    private var eventSelection = ""
    private var eventSelectionWithDates =
        "(${Events.DTSTART} >= ? AND (${Events.DTEND} <= ? OR ${Events.DTEND} IS NULL))"
    private val eventProjection = arrayOf(
        Events.UID_2445,          //0
        Events.CALENDAR_ID,       //1
        Events.TITLE,             //2
        Events.DESCRIPTION,       //3
        Events.DTSTART,           //4
        Events.DTEND,             //5
        Events.EVENT_LOCATION,    //6
        Events.ALL_DAY,           //7
        Events.RRULE,             //8
        Events.DURATION           //9
    )
    private var calendarIdQuery = "${Events.CALENDAR_ID} IN ?"
    private val eventSelectionArgs = listOf<String>()
    private val eventsSortOrder = "${Events.DTSTART} ASC"

    /**
     * Retrieves calendar informations from the base calendar application on the user's device
     * that matches the Google mail logged into the application.
     */
    fun init() {
        val millis = getMonthsStartEndMillis(getToday())
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

        calendarList.forEachIndexed { index, calendarEntity ->
            eventSelectionArgs.plus(calendarEntity.id)
            //Update Selection if more than 1 calendar available
            if (index > 0)
                calendarIdQuery += ",?"
        }
        eventSelection = calendarIdQuery
    }

    /**
     * Retrieves given date's or month's events (by CalendarContract.Instances) from the
     * base calendar application on the user's device that matches the Google mail logged
     * into the application.
     */
    fun getEvents(
        givenDate: LocalDate? = getToday(),
        getDailyEvents: Boolean = false
    ): MutableList<EventEntity> {
        val millis = if (getDailyEvents) getStartEndMillis(givenDate)
                     else getMonthsStartEndMillis(givenDate)
        val builder: Uri.Builder = Instances.CONTENT_URI.buildUpon()
        ContentUris.appendId(builder, millis.first())
        ContentUris.appendId(builder, millis.last())
        val eventCursor: Cursor? = try {
            contentResolver?.query(builder.build(), instanceProjection, null, null, null)
        } catch (e: Exception) {
            App.mCrashlytics?.recordException(e)
            null
        }

        val eventList: MutableList<EventEntity> = mutableListOf()
        while (eventCursor?.moveToNext() == true)
            eventList.add(eventEntity(eventCursor).apply {
                attendees = getAttendees(id)
            })
        return eventList
    }

    /**
     * Retrieves attendee info from base calendar application on the user's device
     */
    private fun getAttendees(eventID: String? = null): MutableList<AttendeeEntity> {
        val cursor: Cursor? = try {
            contentResolver?.query(
                attendeeContentUri,
                attendeeProjection,
                attendeesQuery(eventID),
                null,
                null
            )
        } catch (e: Exception) {
            App.mCrashlytics?.recordException(e)
            null
        }

        val attendeeList: MutableList<AttendeeEntity> = mutableListOf()
        while (cursor?.moveToNext() == true)
            attendeeList.add(attendeeEntity(cursor))
        return attendeeList
    }

    /**
     * Retrieves given date's or month's events from the base calendar application on the user's
     * device that matches the Google mail logged into the application.
     */
    fun getEventsFromGivenDate(
        givenDate: LocalDate? = getToday(),
        getAllMonths: Boolean = false
    ): MutableList<EventEntity> {
        val millis = if (getAllMonths) getMonthsStartEndMillis(givenDate)
        else getStartEndMillis(givenDate)
        val updatedArgs = eventSelectionArgs.plus(millis.first().toString())
            .plus(millis.last().toString())

        if (eventSelectionWithDates.contains(calendarIdQuery).not())
            eventSelectionWithDates = "$eventSelection AND $eventSelectionWithDates"

        val eventCursor: Cursor? = try {
            contentResolver?.query(
                eventContentUri, eventProjection, eventSelectionWithDates,
                updatedArgs.toTypedArray(), eventsSortOrder
            )
        } catch (e: Exception) {
            App.mCrashlytics?.recordException(e)
            null
        }

        val eventList: MutableList<EventEntity> = mutableListOf()
        while (eventCursor?.moveToNext() == true)
            eventList.add(eventEntity(eventCursor))

        return eventList
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
            dtStart = eventCursor.getString(5),
            dtEnd = eventCursor.getString(6),
            begin = eventCursor.getString(7),
            end = eventCursor.getString(8),
            isAllDay = eventCursor.getString(9) != "0",
            rRule = eventCursor.getString(10),
            rDate = eventCursor.getString(11),
            duration = eventCursor.getString(12),
            location = eventCursor.getString(13),
            displayColor = eventCursor.getString(14),
            selfAttendeeStatus = eventCursor.getString(15),
            organizer = eventCursor.getString(16),
            idk = eventCursor.getString(17) != "0",
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
            dtStart = eventCursor.getString(2),    //Instances.DTSTART
            dtEnd = eventCursor.getString(3),      //Instances.DTEND
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
        ContentResolver.requestSync(account,cont CalendarContract.AUTHORITY, extras)
    }

    private fun forceSync(context: Context) {
        val values = ContentValues()
        values.put(CalendarContract.Calendars.SYNC_EVENTS, 1)
        values.put(CalendarContract.Calendars.VISIBLE, 1)

        cr.update(
            ContentUris.withAppendedId(CalendarContract.Calendars.CONTENT_URI,
                3), values, null, null)
    }

    public void syncCalendars()
    {
        Account[] accounts = AccountManager.get(context).getAccounts();
        Log.d(TAG, "Refreshing " + accounts.length + " accounts");
        String authority = CalendarContract.Calendars.CONTENT_URI.getAuthority();
        for (int i = 0; i < accounts.length; i++) {

            Log.d(TAG, "Refreshing calendars for: " + accounts[i]);

            Bundle extras = new Bundle();
            extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
            ContentResolver.requestSync(accounts[i], authority, extras);
        }
    }
     */

}