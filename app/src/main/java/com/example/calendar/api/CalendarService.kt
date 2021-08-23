package com.example.calendar.api

import com.example.calendar.Model.Calendar
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface CalendarService {
    @GET("calender")
    suspend fun getCalendar(
        @Header("Authorization") USER_ID: String
    ): Response<Calendar>

    @POST("calender")
    suspend fun postCalendar(
        @Header("Authorization") USER_ID: String,
        @Body params : HashMap<String, Any>
    )

    @PUT("calender/{id}")
    suspend fun putCalendar(
        @Path ("id") id:String,
        @Header("Authorization") USER_ID: String,
        @Body params : HashMap<String, Any>
    )
}