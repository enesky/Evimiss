package com.enesky.evimiss.data.model

import android.os.Parcelable
import com.enesky.evimiss.utils.*
import kotlinx.parcelize.Parcelize
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

@Parcelize
data class EventEntity(
    var id: String? = "",
    val calendarId: String? = "",
    val title: String? = "",
    val description: String? = "",
    var timeZone: String? = "",
    var begin: String? = null,
    var end: String? = null,
    val isAllDay: Boolean? = false,
    val rRule: String? = "",
    val rDate: String? = "",
    val duration: String? = "",
    val location: String? = "",
    val displayColor: String? = "",
    val selfAttendeeStatus: String? = "",
    val organizer: String? = "",
    val idk: Boolean? = false,
    var attendees: MutableList<AttendeeEntity>? = mutableListOf()
) : Parcelable {

    val startDateTime: LocalDateTime?
        get() = begin?.toLong()?.convert2LocalDateTime()

    val endDateTime: LocalDateTime?
        get() = end?.toLong()?.convert2LocalDateTime()

    val startDate: LocalDate?
        get() = begin?.toLong()?.convert2LocalDate()

    val endDate: LocalDate?
        get() = end?.toLong()?.convert2LocalDate()

    val dateList: MutableList<LocalDate>
        get() = millisInternalDates(startMillis = begin?.toLong() ?: 0L, endMillis = end?.toLong() ?: 0L)

    override fun toString(): String =
                "ID: $id, CalendarID: $calendarId, Title: $title, Description: $description, " +
                "StartDateTime: ${startDateTime?.convert2DetailedDateTime()}, " + "begin: $begin, " +
                "EndDateTime: ${endDateTime?.convert2DetailedDateTime()}, " + "end: $end, " +
                "IsAllDay: $isAllDay, RRule: $rRule, RDate: $rDate, Duration: $duration" +
                "Location: $location, displayColor: $displayColor, selfAttendeeStatus: $selfAttendeeStatus, idk: $idk" +
                "organizer: $organizer, attendees: $attendees"

    fun isEventInGivenDate(givenDate: LocalDate): Boolean = dateList.contains(givenDate)

}