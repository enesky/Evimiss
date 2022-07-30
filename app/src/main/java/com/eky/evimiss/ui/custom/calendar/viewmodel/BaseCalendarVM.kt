package com.eky.evimiss.ui.custom.calendar.viewmodel

import androidx.lifecycle.ViewModel
import com.eky.evimiss.data.model.MyDate
import com.eky.evimiss.ui.custom.calendar.MyCalendarViewState
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by Enes Kamil YILMAZ on 30/07/2022
 */

abstract class BaseCalendarVM : ViewModel() {

    abstract fun getViewState(): StateFlow<MyCalendarViewState>
    abstract fun isBack2TodayAvailable(): Boolean
    abstract fun onDateSelected(selectedDate: MyDate)
    abstract fun onNextMonthClicked()
    abstract fun onPreviousMonthClicked()
    abstract fun onBackToTodayClicked()

}