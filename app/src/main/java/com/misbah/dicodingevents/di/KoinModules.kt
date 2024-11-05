package com.misbah.dicodingevents.di

import androidx.room.Room
import com.misbah.dicodingevents.data.EventRepository
import com.misbah.dicodingevents.data.local.room.EventDatabase
import com.misbah.dicodingevents.data.remote.retrofit.ApiConfig
import com.misbah.dicodingevents.ui.EventViewModel
import com.misbah.dicodingevents.ui.MainViewModel
import com.misbah.dicodingevents.ui.SettingPreferences
import com.misbah.dicodingevents.ui.dataStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            EventDatabase::class.java, "Event.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single { ApiConfig.getApiService() }
    single { get<EventDatabase>().eventDao() }
    single { EventRepository(apiService = get(), eventDao =  get() )}

    single { SettingPreferences(androidContext().dataStore) }
    viewModel { MainViewModel(get()) }
    viewModel { EventViewModel(get()) }
}