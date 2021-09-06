package com.enesky.evimiss.ui.custom.calendar

import org.threeten.bp.*
import org.threeten.bp.format.TextStyle
import java.util.*

/**
 * Created by Enes Kamil YILMAZ on 05/09/2021
 */

data class MyDate(
    var date: LocalDate,
    var dayOfWeek: String = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("TR")), // "Pzt"
    var dayOfWeekValue: Int = date.dayOfWeek.value, // 1
    var dayOfMonth: Int = date.dayOfMonth,// 1
    var month: String = date.month.getDisplayName(TextStyle.FULL, Locale("TR")),// "Eylül",
    var hasEvents: Boolean = dayOfMonth % 5 == 0 //false
)
