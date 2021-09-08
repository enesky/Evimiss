package com.enesky.evimiss.ui.custom.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enesky.evimiss.utils.getMonthList
import com.enesky.evimiss.utils.getTodaysMyDate
import com.enesky.evimiss.utils.getWeeksOfMonth
import com.enesky.evimiss.utils.isFromThisMonth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

class MyCalendarVM: ViewModel() {

    data class MyCalendarViewState(
        var currentDate: MyDate = getTodaysMyDate(),
        var title: String = currentDate.month,
        var weekLists: MutableList<Pair<Int, List<MyDate>>> = getMonthList(date = currentDate.date).getWeeksOfMonth(),
        var selectedDate: MyDate = currentDate
    )

    private val _myCalendarViewState = MutableStateFlow(MyCalendarViewState())
    fun myCalendarViewState() = _myCalendarViewState.asStateFlow()

    fun isSelectedDateInitial() = _myCalendarViewState.value.selectedDate.date == LocalDate.MIN

    fun onDateSelected(selectedDate: MyDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentDate = MyDate(myCalendarViewState().value.currentDate.date)
            if (selectedDate.date.isFromThisMonth(currentDate.date.month).not()) {
                _myCalendarViewState.update {
                    it.copy(
                        selectedDate = selectedDate,
                        currentDate = selectedDate,
                        title = selectedDate.month,
                        weekLists = getMonthList(selectedDate.date).getWeeksOfMonth()
                    )
                }
            } else {
                _myCalendarViewState.update { it.copy(selectedDate = selectedDate) }
            }
        }
    }

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
                    title = currentDate.month,
                    weekLists = getMonthList(currentDate.date).getWeeksOfMonth(),
                    selectedDate = currentDate
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
