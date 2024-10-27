package com.misbah.dicodingevents.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {
    fun getTheme() = pref.getThemeSetting().asLiveData()

    fun saveTheme(isDark: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDark)
        }
    }

    fun getReminder() = pref.getReminderSetting().asLiveData()

    fun saveDailyReminder(isReminder: Boolean) {
        viewModelScope.launch {
            pref.saveReminderSetting(isReminder)
        }
    }
}