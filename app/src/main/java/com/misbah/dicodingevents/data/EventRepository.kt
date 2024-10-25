package com.misbah.dicodingevents.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.misbah.dicodingevents.data.local.entity.EventEntity
import com.misbah.dicodingevents.data.local.room.EventDao
import com.misbah.dicodingevents.data.remote.retrofit.ApiService

class EventRepository private constructor(private val apiService: ApiService, private val eventDao: EventDao) {

    fun getEventsActive(): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)

        try {
            val response = apiService.getEvents(1)
            val events = response.listEvents
            val isActive = true
            val eventList = events.map { event ->
                val isFavorite = eventDao.isEventFavorite(event.name)

                EventEntity(
                    event.id,
                    event.name,
                    event.summary,
                    event.category,
                    event.description,
                    event.ownerName,
                    event.quota,
                    event.registrants,
                    event.beginTime,
                    event.endTime,
                    event.link,
                    event.imageLogo,
                    event.mediaCover,
                    isFavorite,
                    isActive
                )
            }
            // insert to room db
            eventDao.insertEvents(eventList)
        } catch (e: Exception) {
            Log.d("EventRepository", "getEventsActive: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
        // get all event active from room db
        val localData: LiveData<Result<List<EventEntity>>> = eventDao.getEventsActive().map { Result.Success(it) }
        emitSource(localData)
    }

    fun getEventsFinished(): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)

        try {
            val response = apiService.getEvents(0)
            val events = response.listEvents
            val isActive = false
            val eventList = events.map { event ->
                val isFavorite = eventDao.isEventFavorite(event.name)

                EventEntity(
                    event.id,
                    event.name,
                    event.summary,
                    event.category,
                    event.description,
                    event.ownerName,
                    event.quota,
                    event.registrants,
                    event.beginTime,
                    event.endTime,
                    event.link,
                    event.imageLogo,
                    event.mediaCover,
                    isFavorite,
                    isActive
                )
            }
            // insert to room db
            eventDao.insertEvents(eventList)
        } catch (e: Exception) {
            Log.d("EventRepository", "getEventsFinished: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
        // get all event finished from room db
        val localData: LiveData<Result<List<EventEntity>>> = eventDao.getEventsFinished().map { Result.Success(it) }
        emitSource(localData)
    }

    fun getEventById(id: Int): LiveData<EventEntity> {
        return eventDao.getEventById(id)
    }

    fun getEventFavorite(): LiveData<List<EventEntity>> {
        return eventDao.getFavoriteEvents()
    }

    suspend fun setEventFavorite(event: EventEntity, favoriteState: Boolean) {
        event.isFavorite = favoriteState
        eventDao.updateEvent(event)
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null

        fun getInstance(apiService: ApiService, eventDao: EventDao): EventRepository {
            return instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, eventDao)
            }.also { instance = it }
        }
    }
}