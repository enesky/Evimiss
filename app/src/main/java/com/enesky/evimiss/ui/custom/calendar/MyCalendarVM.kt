package com.enesky.evimiss.ui.custom.calendar

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enesky.evimiss.App
import com.enesky.evimiss.utils.*
import com.enesky.evimiss.utils.getTodaysMyDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

class MyCalendarVM(
    private val contentResolver: ContentResolver?
): ViewModel() {

    private val initialViewState = MyCalendarViewState(eventList = getEventList(getTodaysMyDate().date))

    private val _myCalendarViewState = MutableStateFlow(initialViewState)
    fun myCalendarViewState() = _myCalendarViewState.asStateFlow()

    fun isSelectedDateInitial() = _myCalendarViewState.value.selectedDate.date == LocalDate.MIN

    fun isBack2TodayAvailable(): Boolean =
        myCalendarViewState().value.currentDate.date.isDatesEqueal().not()

    fun onDateSelected(selectedDate: MyDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentDate = MyDate(myCalendarViewState().value.currentDate.date)
            if (selectedDate.date.isFromThisMonth(currentDate.date).not()) {
                _myCalendarViewState.update {
                    it.copy(
                        selectedDate = selectedDate,
                        currentDate = selectedDate,
                        title = selectedDate.month,
                        weekLists = getMonthList(selectedDate.date).getWeeksOfMonth(),
                        eventList = getEventList(selectedDate.date)
                    )
                }
            } else {
                _myCalendarViewState.update {
                    it.copy(
                        selectedDate = selectedDate,
                        currentDate = selectedDate,
                        eventList = getEventList(selectedDate.date)
                    )
                }
            }
        }
    }

    private fun getEventList(givenDate: LocalDate) = App.mCalendarUtil?.getCalendarEventsInGivenDate(
        contentResolver = contentResolver,
        givenDate = givenDate
    ) ?: mutableListOf()

    fun onNextMonthClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentDate = MyDate(myCalendarViewState().value.currentDate.date.plusMonths(1))
            _myCalendarViewState.update {
                it.copy(currentDate = currentDate,
                    title = currentDate.month,
                    weekLists = getMonthList(currentDate.date).getWeeksOfMonth()
                )
            }
        }
    }

    fun onPreviousMonthClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentDate = MyDate(myCalendarViewState().value.currentDate.date.minusMonths(1))
            _myCalendarViewState.update {
                it.copy(currentDate = currentDate,
                        title = currentDate.month,
                        weekLists = getMonthList(currentDate.date).getWeeksOfMonth()
                )
            }
        }
    }

    fun onBackToTodayClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentDate = getTodaysMyDate()
            _myCalendarViewState.update {
                it.copy(
                    currentDate = currentDate,
                    selectedDate = currentDate,
                    title = currentDate.month,
                    weekLists = getMonthList(currentDate.date).getWeeksOfMonth(),
                )
            }
        }
    }

}

inline fun <T> MutableStateFlow<T>.update(function: (T) -> T) {
    while (true) {
        val prevValue = value
        val nextValue = function(prevValue)
        if (compareAndSet(prevValue, nextValue)) {
            return
        }
    }
}
