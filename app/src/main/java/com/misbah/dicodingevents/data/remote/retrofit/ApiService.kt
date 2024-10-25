package com.misbah.dicodingevents.data.remote.retrofit

import com.misbah.dicodingevents.data.remote.response.EventResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getEvents(@Query("active") active: Int): EventResponse
}