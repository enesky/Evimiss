package com.enesky.evimiss

import com.enesky.evimiss.utils.getMonthList
import com.enesky.evimiss.utils.getWeeksOfMonth
import com.enesky.evimiss.utils.isToday
import org.junit.Test

import org.junit.Assert.*
import org.threeten.bp.LocalDate

class DateUnitTest {

    @Test
    fun isTodayCorrect() {
        assertEquals(true, isToday(LocalDate.now().toString()))
    }

    @Test
    fun isTodayFail() {
        assertEquals(false, isToday("2020-05-05"))
    }

    @Test
    fun getMonthInfoCorrect() {
        assertEquals(true, getMonthList())
    }

    @Test
    fun getDayNameList() {
        val weekNameList = getDayNameList()
        println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
        println(weekNameList.toString())
        println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
    }

    @Test
    fun getWeeksOfMonth() {
        val weekList = getMonthList().getWeeksOfMonth()
        println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
        for (day in weekList)
            println(day.toString())
        println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
    }

}