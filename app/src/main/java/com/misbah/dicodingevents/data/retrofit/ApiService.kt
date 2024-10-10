package com.misbah.dicodingevents.data.retrofit

import com.misbah.dicodingevents.data.response.Event
import com.misbah.dicodingevents.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getEvents(@Query("active") active: Int): Call<EventResponse>

    @GET("events/{id}")
    fun getEventById(
        @Path("id") id: Int,
    ): Call<Event>
}