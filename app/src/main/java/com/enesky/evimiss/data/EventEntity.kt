package com.enesky.evimiss.data

import android.os.Parcelable
import com.enesky.evimiss.utils.convert2DetailedDateTime
import com.enesky.evimiss.utils.convert2LocalDateTime
import kotlinx.parcelize.Parcelize
import org.threeten.bp.LocalDateTime

@Parcelize
data class EventEntity(
    var id: String? = "",
    val calendarId: String? = "",
    val title: String? = "",
    val description: String? = "",
    val dtStart: String? = null,
    val dtEnd: String? = null,
    val location: String? = "",
    val isAllDay: Boolean? = false,
    val rRule: String? = "",
    val duration: String? = ""
) : Parcelable {

    val startDateTime: LocalDateTime?
        get() = if (dtStart.isNullOrEmpty()) null else dtStart.toLong().convert2LocalDateTime()

    val endDateTime: LocalDateTime?
        get() = if (dtEnd.isNullOrEmpty()) null else dtEnd.toLong().convert2LocalDateTime()

    override fun toString(): String =
        "ID: $id, CalendarID: $calendarId, Title: $title, Description: $description, " +
                "StartDateTime: ${startDateTime?.convert2DetailedDateTime()}, " + "dtStart: $dtStart, " +
                "EndDateTime: ${endDateTime?.convert2DetailedDateTime()}, " + "dtEnd: $dtEnd, " +
                "Location: $location, IsAllDay: $isAllDay, " +
                "RRule: $rRule, Duration: $duration"
}
