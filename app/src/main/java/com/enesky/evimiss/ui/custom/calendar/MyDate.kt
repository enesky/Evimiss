package com.enesky.evimiss.ui.custom.calendar

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.threeten.bp.*
import org.threeten.bp.format.TextStyle
import java.util.*

/**
 * Created by Enes Kamil YILMAZ on 05/09/2021
 */

@Parcelize
data class MyDate(
    var date: LocalDate,
    var dayOfWeek: String = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()), // "Pzt"
    var dayOfWeekValue: Int = date.dayOfWeek.value, // 1
    var dayOfMonth: Int = date.dayOfMonth,// 1
    var month: String = date.month.getDisplayName(TextStyle.FULL, Locale.getDefault()),// "Eylül",
    var events: List<Int> = (0..(1..3).random()).toList(),
    var hasEvents: Boolean = dayOfMonth % (1..10).random() == 0, //false
    var time: LocalTime = LocalTime.MIN,
    var dateTime: LocalDateTime = LocalDateTime.of(date, time)
) : Parcelable

@Parcelize
data class Event(
    var date: LocalDate,
    var users: List<User>? = listOf(),
    var details: String? = "",
    var attachments: String? = "",
    var isExpense: Boolean = false,
    var hasAlarm: Boolean = false
) : Parcelable

@Parcelize
data class User (
    var username: String,
    var email: String,
) : Parcelable