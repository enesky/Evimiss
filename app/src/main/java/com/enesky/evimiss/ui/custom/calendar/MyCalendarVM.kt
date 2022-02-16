package com.enesky.evimiss.ui.custom.calendar

import androidx.lifecycle.*
import com.enesky.evimiss.data.model.EventEntity
import com.enesky.evimiss.data.model.MyDate
import com.enesky.evimiss.data.repo.CalendarRepository
import com.enesky.evimiss.data.repo.EventsInterval
import com.enesky.evimiss.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import javax.inject.Inject

@HiltViewModel
class MyCalendarVM @Inject constructor(
    private val calendarRepository: CalendarRepository
) : ViewModel() {

    private val _myCalendarViewState = MutableStateFlow(MyCalendarViewState().apply {
        updateEvents(
            calendarRepository.getEvents(eventsInterval = EventsInterval.Quarterly)
        )
    })
    fun myCalendarViewState() = _myCalendarViewState.asStateFlow()

    fun isBack2TodayAvailable(): Boolean = myCalendarViewState().value.todaysDate.date.isDatesEqual().not()

    fun onDateSelected(selectedDate: MyDate) {
        viewModelScope.launch {
            val oldDate = _myCalendarViewState.value.selectedDate
            if (selectedDate.date.isFromThisMonth().not()) {
                _myCalendarViewState.update {
                    it.copy(
                        selectedDate = selectedDate,
                        todaysDate = selectedDate,
                        calendarTitle = selectedDate.month,
                    ).apply {
                        val givenDate = if (oldDate.date.isAfter(selectedDate.date))
                                            oldDate.date.plusMonths(1)
                                        else
                                            oldDate.date.minusMonths(1)
                        it.copy().updateEvents(
                            newMap = calendarRepository.getEvents(
                                givenDate = givenDate,
                                eventsInterval = EventsInterval.Monthly
                            )
                        )
                    }
                }
        } else {
                _myCalendarViewState.update {
                    it.copy(
                        selectedDate = selectedDate,
                        todaysDate = selectedDate
                    )
                }
            }
        }
    }

    fun getEventList(
        givenDate: LocalDate? = null,
        eventsInterval: EventsInterval? = null
    ): LiveData<MutableMap<LocalDate, MutableList<EventEntity>>> = liveData {
        emit(calendarRepository.fetchEvents(givenDate, eventsInterval))
    }

    fun onNextMonthClicked() {
        viewModelScope.launch {
            val currentDate = MyDate(myCalendarViewState().value.todaysDate.date.plusMonths(1))
            _myCalendarViewState.update {
                it.copy(
                    todaysDate = currentDate,
                    calendarTitle = currentDate.month
                ).apply {
                    if (it.eventMap.containsKey(currentDate.date.plusMonths(1)).not()) {
                        it.copy().updateEvents(
                            newMap = calendarRepository.getEvents(
                                givenDate = currentDate.date.plusMonths(1),
                                eventsInterval = EventsInterval.Monthly
                            )
                        )
                    }
                }
            }
        }
    }

    fun onPreviousMonthClicked() {
        viewModelScope.launch {
            val currentDate = MyDate(myCalendarViewState().value.todaysDate.date.minusMonths(1))
            _myCalendarViewState.update {
                it.copy(
                    todaysDate = currentDate,
                    calendarTitle = currentDate.month,
                ).apply {
                    if (it.eventMap.containsKey(currentDate.date.minusMonths(1)).not()) {
                        it.copy().updateEvents(
                            newMap = calendarRepository.getEvents(
                                givenDate = currentDate.date.minusMonths(1),
                                eventsInterval = EventsInterval.Monthly
                            )
                        )
                    }
                }
            }
        }
    }

    fun onBackToTodayClicked() {
        viewModelScope.launch {
            val currentDate = getTodaysMyDate()
            _myCalendarViewState.update {
                it.copy(
                    todaysDate = currentDate,
                    selectedDate = currentDate,
                    calendarTitle = currentDate.month
                )
            }
        }
    }

}
