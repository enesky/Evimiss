package com.enesky.evimiss.ui.custom.calendar

import org.threeten.bp.DayOfWeek
import org.threeten.bp.Month
import org.threeten.bp.Year
import org.threeten.bp.YearMonth

/**
 * Created by Enes Kamil YILMAZ on 05/09/2021
 */

data class MyDay(
    val dayOfWeek: DayOfWeek,
    val dayOfMonth: Month,
    val month: YearMonth,
    val year: Year
) {

}
