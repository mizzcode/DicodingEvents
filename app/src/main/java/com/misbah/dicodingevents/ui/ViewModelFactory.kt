package com.misbah.dicodingevents.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.misbah.dicodingevents.data.EventRepository
import com.misbah.dicodingevents.di.Injection

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(private val eventRepository: EventRepository, private val settingPreferences: SettingPreferences): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
            return  EventViewModel(eventRepository) as T
        }
        else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(settingPreferences) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory {
            val settingPreferences = SettingPreferences.getInstance(context.dataStore)

            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context), settingPreferences).also { instance = it }
            }
        }
    }
}