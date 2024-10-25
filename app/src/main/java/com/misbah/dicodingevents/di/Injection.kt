package com.misbah.dicodingevents.di

import android.content.Context
import com.misbah.dicodingevents.data.EventRepository
import com.misbah.dicodingevents.data.local.room.EventDatabase
import com.misbah.dicodingevents.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val dao = database.eventDao()
        return EventRepository.getInstance(apiService, dao)
    }
}