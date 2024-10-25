package com.misbah.dicodingevents.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.misbah.dicodingevents.data.local.entity.EventEntity

@Dao
interface EventDao {
    @Query("SELECT * FROM events WHERE isActive = 1")
    fun getEventsActive(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM events WHERE isActive = 0")
    fun getEventsFinished(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM events where isFavorite = 1")
    fun getFavoriteEvents(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM events WHERE id = :id")
    fun getEventById(id: Int): LiveData<EventEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEvents(news: List<EventEntity>)

    @Update
    suspend fun updateEvent(news: EventEntity)

    @Query("DELETE FROM events WHERE isFavorite = 0")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM events WHERE name = :title AND isFavorite = 1)")
    suspend fun isEventFavorite(title: String): Boolean
}