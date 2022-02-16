package com.enesky.evimiss.data.repo

import com.enesky.evimiss.data.model.EventEntity
import com.enesky.evimiss.utils.getToday
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import javax.inject.Inject

/**
 * Created by Enes Kamil YILMAZ on 14/02/2022
 */

class CalendarRepository @Inject constructor(
    private val source: CalendarDataSource
) {

    suspend fun fetchEvents(
        givenDate: LocalDate? = null,
        eventsInterval: EventsInterval? = null
    ): MutableMap<LocalDate, MutableList<EventEntity>> {
        return withContext(Dispatchers.Default) {
            source.getEvents(givenDate, eventsInterval)
        }
    }

    fun getEvents(
        givenDate: LocalDate? = null,
        eventsInterval: EventsInterval? = null
    ): MutableMap<LocalDate, MutableList<EventEntity>> {
        return source.getEvents(givenDate, eventsInterval)
    }

}