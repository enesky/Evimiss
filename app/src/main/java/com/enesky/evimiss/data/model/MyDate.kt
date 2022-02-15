package com.enesky.evimiss.data.model

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
    var events: MutableList<EventEntity> = mutableListOf(),
    var hasEvents: Boolean = events.isNotEmpty(),
    var time: LocalTime = LocalTime.MIN,
    var dateTime: LocalDateTime = LocalDateTime.of(date, time)
) : Parcelable