package com.misbah.dicodingevents.ui.upcoming

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.misbah.dicodingevents.data.response.Event
import com.misbah.dicodingevents.data.response.EventResponse
import com.misbah.dicodingevents.data.response.ListEventsItem
import com.misbah.dicodingevents.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingViewModel : ViewModel() {

    private val _textHeading = MutableLiveData<String>().apply {
        value = "Dicoding Event"
    }
    val textHeading: LiveData<String> = _textHeading

    private val _textHeadingDesc = MutableLiveData<String>().apply {
        value = "Recommendation event for you!"
    }
    val textHeadingDesc: LiveData<String> = _textHeadingDesc

    private val _listEvents = MutableLiveData<List<ListEventsItem>>()
    val listEvents: LiveData<List<ListEventsItem>> = _listEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _event = MutableLiveData<ListEventsItem>()
    val event: LiveData<ListEventsItem> = _event

    init {
        findEventActive()
    }

    private fun findEventActive() {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getEvents(1)

        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _listEvents.value = response.body()?.listEvents
                } else {
                    Log.d("UpcomingViewModel", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false

                Log.d("UpcomingViewModel", "onFailure: ${t.message}")
            }

        })
    }

    fun getEventById(eventId: Int) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getEventById(eventId)

        client.enqueue(object : Callback<Event> {
            override fun onResponse(
                call: Call<Event?>,
                response: Response<Event?>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _event.value = response.body()?.event
                } else {
                    Log.d("UpcomingViewModel", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(
                call: Call<Event?>,
                t: Throwable
            ) {
                _isLoading.value = false

                Log.d("UpcomingViewModel", "onFailure: ${t.message}")
            }
        })
    }
}