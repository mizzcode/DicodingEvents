package com.misbah.dicodingevents.ui.finished

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.misbah.dicodingevents.data.response.EventResponse
import com.misbah.dicodingevents.data.response.ListEventsItem
import com.misbah.dicodingevents.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedViewModel : ViewModel() {

    private val _textHeading = MutableLiveData<String>().apply {
        value = "Dicoding Event"
    }
    val textHeading: LiveData<String> = _textHeading

    private val _textHeadingDesc = MutableLiveData<String>().apply {
        value = "Event Finished"
    }
    val textHeadingDesc: LiveData<String> = _textHeadingDesc

    private val _listEvents = MutableLiveData<List<ListEventsItem>>()
    val listEvents: LiveData<List<ListEventsItem>> = _listEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _event = MutableLiveData<ListEventsItem>()
    val event: LiveData<ListEventsItem> = _event

    init {
        findEventNonActive()
    }

    private fun findEventNonActive() {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getEvents(0)

        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _listEvents.value = response.body()?.listEvents
                } else {
                    Log.d("FinishedViewModel", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false

                Log.d("FinishedViewModel", "onFailure: ${t.message}")
            }
        })
    }
}