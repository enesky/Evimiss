package com.enesky.evimiss.utils

import com.enesky.evimiss.ui.custom.calendar.MyDate
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import java.util.*
import kotlin.Exception as KotlinException

const val DATE_FORMAT = "yyyy-MM-dd"
const val TEST_DATE = "2021-09-06"

fun isToday(givenDate: String): Boolean {
    givenDate.convert2FormattedLocalDate().apply {
       return try {
            isEqual(getToday())
        } catch (e: KotlinException) { //Used cause of ZoneRulesException on Preview
            isEqual(TEST_DATE.convert2FormattedLocalDate())
        }
    }
}

fun isToday(givenDate: LocalDate): Boolean {
    givenDate.apply {
        return try {
            isEqual(getToday())
        } catch (e: KotlinException) { //Used cause of ZoneRulesException on Preview
            isEqual(TEST_DATE.convert2FormattedLocalDate())
        }
    }
}

fun getMonthList(date: LocalDate = getToday()): MutableList<MyDate> {
    val listOfThisMonthsDate = mutableListOf<MyDate>()
    for (day in 1..date.lengthOfMonth())
        listOfThisMonthsDate.add(
            MyDate(date= LocalDate.of(date.year, date.month, day))
        )
    return listOfThisMonthsDate
}

fun getDaynames(): List<String> { //listOf("Pzt", "Salı", "Çarş", "Perş", "Cuma", "Cmt", "Pzr")
    val today = getToday()
    val dayList = mutableListOf(today)
    val weekList = mutableListOf<Pair<Int, String>>()

    for (dayNo in 1..6)
        dayList.add(today.plusDays(dayNo.toLong()))

    for (day in dayList)
        weekList.add(
            Pair(
                first = day.dayOfWeek.value,
                second = day.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("TR"))
            )
        )
    weekList.sortBy { it.first }
    return weekList.map { it.second }
}

fun MutableList<MyDate>.getWeeksOfMonth(): MutableList<Pair<Int, MutableList<MyDate>>> {
    val weeks: MutableList<Pair<Int, MutableList<MyDate>>> = mutableListOf()
    val firstDay = this.first()

    val firstWeek = mutableListOf<MyDate>().apply {
        if (firstDay.dayOfWeekValue > 1) {
            for (prevMonthsDay in (firstDay.dayOfWeekValue - 1) downTo 1)
                add(MyDate(date = firstDay.date.minusDays(prevMonthsDay.toLong())))
            for (currentMonthsDay in 0..(7 - firstDay.dayOfWeekValue))
                add(MyDate(date = firstDay.date.plusDays(currentMonthsDay.toLong())))
        } else {
            for (currentMonthsDay in 0..7)
                add(MyDate(date = firstDay.date.plusDays(currentMonthsDay.toLong())))
        }
        weeks.add(Pair(first = 0, second = this))
    }

    val firstWeeksLast = firstWeek.last()
    val lastDayInMonth = firstDay.date.lengthOfMonth()
    val tempDayList = mutableListOf<MyDate>()
    for (dayNo in firstWeeksLast.date.dayOfMonth until lastDayInMonth)
        tempDayList.add((MyDate(date = firstDay.date.plusDays(dayNo.toLong()))))
    val otherWeeks = tempDayList.chunked(7)

    for (weekNo in 1..otherWeeks.size)
        weeks.add(Pair(first = weekNo, second = otherWeeks[weekNo-1]) as Pair<Int, MutableList<MyDate>>)

    val lastDayOfMonth = weeks.last().second.last()
    if (lastDayOfMonth.dayOfWeekValue < 7)
        for (dayNo in 1..7-lastDayOfMonth.dayOfWeekValue)
            weeks.last().second.add(MyDate(date = lastDayOfMonth.date.plusDays(dayNo.toLong())))

    return weeks
}

fun getToday(): LocalDate = LocalDate.now() //returns DATE_FORMAT'ted todays date

fun LocalDate.isFromThisMonth(): Boolean = this.month.equals(getToday().month)

fun String.convert2FormattedLocalDate(): LocalDate {
    val format: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
    return LocalDate.parse(this, format)
}