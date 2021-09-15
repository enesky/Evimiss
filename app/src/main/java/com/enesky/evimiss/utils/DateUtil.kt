package com.enesky.evimiss.utils

import com.enesky.evimiss.ui.custom.calendar.MyDate
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import java.util.*
import kotlin.Exception as KotlinException

const val DATE_FORMAT = "yyyy-MM-dd"
const val DETAILED_DATE_FORMAT = "dd MMMM EEEE, yyyy"
const val TIMELINE_DATE = "MMMM, yyyy"
const val TEST_DATE = "2021-09-16"

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
            MyDate(date = LocalDate.of(date.year, date.month, day))
        )
    return listOfThisMonthsDate
}

fun getDayNames(): List<String> { //listOf("Pzt", "Salı", "Çarş", "Perş", "Cuma", "Cmt", "Pzr")
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

fun MutableList<MyDate>.getWeeksOfMonth(): MutableList<Pair<Int, List<MyDate>>> {
    val weeks: MutableList<Pair<Int, List<MyDate>>> = mutableListOf()
    val firstDay = this.first()
    val lastDayInMonth = firstDay.date.lengthOfMonth()
    val tempDayList = mutableListOf<MyDate>()

    for (dayNo in 0 until lastDayInMonth)
        tempDayList.add(MyDate(date = firstDay.date.plusDays(dayNo.toLong())))

    if (firstDay.dayOfWeekValue != 1)
        for (dayNo in 1 until firstDay.dayOfWeekValue)
            tempDayList.add(0, MyDate(date = firstDay.date.minusDays(dayNo.toLong())))

    val lastDayOfMonth = tempDayList.last()
    if (lastDayOfMonth.dayOfWeekValue < 7)
        for (dayNo in 1..7-lastDayOfMonth.dayOfWeekValue)
            tempDayList.add(MyDate(date = lastDayOfMonth.date.plusDays(dayNo.toLong())))

    val otherWeeks = tempDayList.chunked(7)

    for (weekNo in 1..otherWeeks.size)
        weeks.add(Pair(first = weekNo, second = otherWeeks[weekNo-1]))

    return weeks
}

fun getToday(): LocalDate {
    return try {
        LocalDate.now()
    } catch (e: KotlinException) { //Used cause of ZoneRulesException on Preview
        TEST_DATE.convert2FormattedLocalDate()
    }
}

fun getTodaysMyDate(): MyDate {
    return try {
        MyDate(date = LocalDate.now())
    } catch (e: KotlinException) { //Used cause of ZoneRulesException on Preview
        MyDate(date = TEST_DATE.convert2FormattedLocalDate())
    }
}

fun LocalDate.isFromThisMonth(givenDate: LocalDate = getToday()): Boolean = month.equals(givenDate.month) && year == givenDate.year

fun String.convert2FormattedLocalDate(): LocalDate =
    LocalDate.parse(this, DateTimeFormatter.ofPattern(DATE_FORMAT).withLocale(Locale("TR")))

fun String.convert2DetailedLocalDate(): LocalDate =
    LocalDate.parse(this, DateTimeFormatter.ofPattern(DETAILED_DATE_FORMAT).withLocale(Locale("TR")))

fun LocalDate.convert2DetailedDate(): String =
    format(DateTimeFormatter.ofPattern(DETAILED_DATE_FORMAT).withLocale(Locale("TR")))

fun LocalDate.convert2TimelineDate(): String =
    format(DateTimeFormatter.ofPattern(TIMELINE_DATE).withLocale(Locale("TR")))