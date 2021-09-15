package com.example.calendar.Retrofit

import com.example.calendar.api.CalendarService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {

//    companion object{
//        val retrofit = Retrofit.Builder()
//            .baseUrl("http://api.kumas.dev/")
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val service = retrofit.create(CalendarService::class.java)
//    }
}