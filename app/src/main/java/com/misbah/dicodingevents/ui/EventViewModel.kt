package com.misbah.dicodingevents.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misbah.dicodingevents.data.EventRepository
import com.misbah.dicodingevents.data.local.entity.EventEntity
import kotlinx.coroutines.launch

class EventViewModel(private val eventRepository: EventRepository) : ViewModel() {
    fun getEventsActive() = eventRepository.getEventsActive()

    fun getEventsFinished() = eventRepository.getEventsFinished()

    fun getEventById(id: Int) = eventRepository.getEventById(id)

    fun getEventFavorite() = eventRepository.getEventFavorite()

    fun saveEventToFavorite(event: EventEntity){
        viewModelScope.launch {
            eventRepository.setEventFavorite(event, true)
        }
    }

    fun deleteEventFromFavorite(event: EventEntity){
        viewModelScope.launch {
            eventRepository.setEventFavorite(event, false)
        }
    }
}