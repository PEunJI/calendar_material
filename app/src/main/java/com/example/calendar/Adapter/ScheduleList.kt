package com.example.calendar.Adapter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.calendar.Dots.Dots
import com.example.calendar.Retrofit.RetrofitService
import com.example.calendar.kakaoLogin.KakaoLogin
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.ArrayList

data class ScheduleList(
    var title: String? = null,
    var start: String? = null,
    var end: String? = null,
    var memo: String? = null,
    var id: Int? = null
) {

    companion object {

        val MutablescheduleList  = mutableListOf<ScheduleList>()

        fun getScheules() = runBlocking {
            val job = GlobalScope.launch {
                val response = RetrofitService.service.getCalendar("${KakaoLogin.user_id}")
                var responses = response.body()!!
                for (i in responses.result) {
                    val scheduleList = ScheduleList()
                    scheduleList.end = i.dateEnd
                    scheduleList.start = i.dateStart
                    val content= i.content
                    val splitString = content.split("@^")

                    try{ scheduleList.memo = splitString[1]}
                    catch(e : Exception){}
                    scheduleList.title = splitString[0]
                    scheduleList.id = i.id
                    MutablescheduleList.add(scheduleList)
                }
            }
            job.join()
        }

    }


}