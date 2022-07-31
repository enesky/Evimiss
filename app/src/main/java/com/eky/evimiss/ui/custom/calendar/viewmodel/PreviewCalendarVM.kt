package com.eky.evimiss.ui.custom.calendar.viewmodel

import com.eky.evimiss.data.model.MyDate
import com.eky.evimiss.ui.custom.calendar.MyCalendarViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Created by Enes Kamil YILMAZ on 30/07/2022
 */

class PreviewCalendarVM : BaseCalendarVM() {

    override fun getViewState(): StateFlow<MyCalendarViewState> =
        MutableStateFlow(MyCalendarViewState()).asStateFlow()

    override fun onDateSelected(selectedDate: MyDate) = Unit

    override fun onNextMonthClicked() = Unit

    override fun onPreviousMonthClicked() = Unit

    override fun onBackToTodayClicked() = Unit

}