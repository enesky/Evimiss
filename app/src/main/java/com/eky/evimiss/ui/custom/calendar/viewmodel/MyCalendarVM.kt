package com.eky.evimiss.ui.custom.calendar.viewmodel

import androidx.lifecycle.*
import com.eky.evimiss.data.model.EventEntity
import com.eky.evimiss.data.model.MyDate
import com.eky.evimiss.data.repo.CalendarRepository
import com.eky.evimiss.data.repo.EventsInterval
import com.eky.evimiss.ui.custom.calendar.MyCalendarViewState
import com.eky.evimiss.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import javax.inject.Inject

@HiltViewModel
class MyCalendarVM @Inject constructor(
    private val calendarRepository: CalendarRepository
) : BaseCalendarVM() {

    private val _myCalendarViewState = MutableStateFlow(MyCalendarViewState().apply {
        updateEvents(calendarRepository.getEvents(eventsInterval = EventsInterval.Quarterly))
    })
    override fun getViewState() = _myCalendarViewState.asStateFlow()

    override fun onDateSelected(selectedDate: MyDate) {
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

    override fun onNextMonthClicked() {
        viewModelScope.launch {
            val currentDate = MyDate(getViewState().value.todaysDate.date.plusMonths(1))
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

    override fun onPreviousMonthClicked() {
        viewModelScope.launch {
            val currentDate = MyDate(getViewState().value.todaysDate.date.minusMonths(1))
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

    override fun onBackToTodayClicked() {
        viewModelScope.launch {
            if (getViewState().value.todaysDate.date.isDatesEqual())
                return@launch
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
