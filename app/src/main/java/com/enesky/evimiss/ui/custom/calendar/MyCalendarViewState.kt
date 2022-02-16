package com.enesky.evimiss.ui.custom.calendar

import com.enesky.evimiss.data.model.EventEntity
import com.enesky.evimiss.data.model.MyDate
import com.enesky.evimiss.utils.getMonthList
import com.enesky.evimiss.utils.getTodaysMyDate
import com.enesky.evimiss.utils.getWeekList
import com.enesky.evimiss.utils.getWeeksOfMonth
import org.threeten.bp.LocalDate

data class MyCalendarViewState(
    var todaysDate: MyDate = getTodaysMyDate(),
    var calendarTitle: String = todaysDate.month,
    var selectedDate: MyDate = todaysDate,
    var eventMap: MutableMap<LocalDate, MutableList<EventEntity>> = mutableMapOf()
) {

    val weekLists: MutableList<Pair<Int, List<MyDate>>>
        get() = getMonthList(selectedDate.date).getWeeksOfMonth().apply {
            updateWeeks(this)
        }

    fun updateEvents(newMap: MutableMap<LocalDate, MutableList<EventEntity>>): MyCalendarViewState {
        eventMap.putAll(newMap)
        todaysDate.events = newMap[todaysDate.date] ?: mutableListOf()
        selectedDate.events = newMap[selectedDate.date] ?: mutableListOf()
        weekLists
        return this
    }

    private fun updateWeeks(weeks: MutableList<Pair<Int, List<MyDate>>>) {
        for (weekList in weeks)
            for (myDate in weekList.second)
                myDate.events = eventMap[myDate.date] ?: mutableListOf()
    }


}