package com.example.calendar.Dots

import android.util.Log
import androidx.core.net.ParseException
import com.example.calendar.Retrofit.RetrofitService.Companion.service
import com.example.calendar.kakaoLogin.KakaoLogin
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashSet

class Dots {


    companion object {
        //캘린더에 점찍기 위해서 스케줄 있는 날짜만 받아움
        // val calendarDots: MutableList<CalendarDay> = mutableListOf<CalendarDay>() //처음 끝 날짜만
        val calendarDotsAll: HashSet<CalendarDay> = HashSet<CalendarDay>() //처음~끝 날짜 모두


        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")

        fun getDate() = runBlocking {
            //응답받아서 날짜만 MutableList에 넣고, decorator에서 그 날짜에 dots를 찍어준다.
            val job = GlobalScope.launch {
                val response = service.getCalendar("${KakaoLogin.user_id}")
                var responses = response.body()!!
                for (i in responses.result) {
                    ragneDates(formatter.parse(i.dateStart), formatter.parse(i.dateEnd))
                }
            }
            job.join()
        }


        @Throws(ParseException::class)
        @JvmStatic
        fun ragneDates(startDate: Date, endDate: Date) {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val c = Calendar.getInstance()
            var currentDay = startDate
            while (currentDay <= endDate) {
                calendarDotsAll.add(CalendarDay(currentDay))
                c.time = currentDay
                c.add(Calendar.DAY_OF_MONTH, 1)
                currentDay = c.time
            }
            for (date in calendarDotsAll) {
                Log.e("rangedate", date.toString())
            }
        }

    }


}