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
    val dtStart: String? = null,
    var begin: String? = null,
    val dtEnd: String? = null,
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
        get() = if (dtStart.isNullOrEmpty() && begin.isNullOrEmpty().not()) begin?.toLong()?.convert2LocalDateTime()
                else if (dtStart.isNullOrEmpty().not() && begin.isNullOrEmpty()) dtStart?.toLong()?.convert2LocalDateTime()
                else null

    val endDateTime: LocalDateTime?
        get() = if (dtEnd.isNullOrEmpty() && end.isNullOrEmpty().not()) end?.toLong()?.convert2LocalDateTime()
                else if (dtEnd.isNullOrEmpty().not() && end.isNullOrEmpty()) dtEnd?.toLong()?.convert2LocalDateTime()
                else null

    val startDate: LocalDate?
        get() = if (dtStart.isNullOrEmpty() && begin.isNullOrEmpty().not()) begin?.toLong()?.convert2LocalDate()
                else if (dtStart.isNullOrEmpty().not() && begin.isNullOrEmpty()) dtStart?.toLong()?.convert2LocalDate()
                else null

    val endDate: LocalDate?
        get() = if (dtEnd.isNullOrEmpty() && end.isNullOrEmpty().not()) end?.toLong()?.convert2LocalDate()
                else if (dtEnd.isNullOrEmpty().not() && end.isNullOrEmpty()) dtEnd?.toLong()?.convert2LocalDate()
                else null

    override fun toString(): String =
                "ID: $id, CalendarID: $calendarId, Title: $title, Description: $description, " +
                "StartDateTime: ${startDateTime?.convert2DetailedDateTime()}, " + "dtStart: $dtStart, " + "begin: $begin, " +
                "EndDateTime: ${endDateTime?.convert2DetailedDateTime()}, " + "dtEnd: $dtEnd, " + "end: $end, " +
                "IsAllDay: $isAllDay, RRule: $rRule, RDate: $rDate, Duration: $duration" +
                "Location: $location, displayColor: $displayColor, selfAttendeeStatus: $selfAttendeeStatus, idk: $idk" +
                "organizer: $organizer, attendees: $attendees"

    fun isEventInGivenDate(givenDate: LocalDate): Boolean =
        if (startDate == null || endDate == null) false
        else givenDate.isEqual(startDate) || (givenDate.isAfter(startDate) && givenDate.isBefore(endDate))

}