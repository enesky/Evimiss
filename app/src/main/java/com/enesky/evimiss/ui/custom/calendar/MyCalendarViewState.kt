package com.enesky.evimiss.ui.custom.calendar

import com.enesky.evimiss.data.EventEntity
import com.enesky.evimiss.utils.getMonthList
import com.enesky.evimiss.utils.getTodaysMyDate
import com.enesky.evimiss.utils.getWeeksOfMonth

data class MyCalendarViewState(
    var currentDate: MyDate = getTodaysMyDate(),
    var title: String = currentDate.month,
    var weekLists: MutableList<Pair<Int, List<MyDate>>> = getMonthList(date = currentDate.date).getWeeksOfMonth(),
    var selectedDate: MyDate = currentDate,
    var eventList: MutableList<EventEntity> = mutableListOf()
)