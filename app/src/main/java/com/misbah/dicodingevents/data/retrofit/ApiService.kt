package com.misbah.dicodingevents.data.retrofit

import com.misbah.dicodingevents.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("events")
    fun getEvents(): Call<EventResponse>

    @GET("events/{id}")
    fun getEventsById(
        @Path("id") id: Int,
    ): Call<EventResponse>
}