package com.misbah.dicodingevents.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.misbah.dicodingevents.data.local.entity.EventEntity

@Database(entities = [EventEntity::class], version = 2, exportSchema = false)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
}