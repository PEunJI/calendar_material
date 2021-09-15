package com.example.calendar.api

import com.example.calendar.Model.Calendar
import retrofit2.Response
import retrofit2.http.*

interface CalendarService {
    @GET("calendar")
    suspend fun getCalendar(
        @Header("Authorization") USER_ID: String
    ): Response<Calendar>

    @POST("calendar")
    suspend fun postCalendar(
        @Header("Authorization") USER_ID: String,
        @Body params : HashMap<String, String>
    )

    @PUT("calendar/{id}")
    suspend fun putCalendar(
        @Path ("id") id:String,
        @Header("Authorization") USER_ID: String,
        @Body params : HashMap<String, String>
    )

    @DELETE("calendar/{id}")
    suspend fun deleteCalendar(
        @Path ("id") id:String,
        @Header("Authorization") USER_ID: String,
    )
}