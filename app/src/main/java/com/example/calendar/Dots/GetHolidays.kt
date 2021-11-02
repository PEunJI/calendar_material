package com.example.calendar.Dots

import android.content.Context
import android.content.res.Resources
import android.provider.Settings.Global.getString
import android.provider.Settings.Secure.getString
import android.util.Log
import com.example.calendar.R
import okhttp3.*
import com.google.gson.Gson
import com.prolificinteractive.materialcalendarview.CalendarDay


class GetHolidays {

    //전년도, 올해, 내년 (3년간) 휴일 일정 받아옴
    val thisYear = CalendarDay.today().year
    val threeYear = arrayOf(thisYear - 1, thisYear, thisYear + 1)


    fun getHolidays(context: Context) {

        //1.클라이언트를 만들기
        val clinet = OkHttpClient.Builder().build()
        //2.요청만들기
        for (i in threeYear) {
            val holidayKey = context.getString(R.string.holidayKey)
            val url =
                "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo?solYear=${i}&ServiceKey=${holidayKey.toString()}&_type=json&numOfRows=100"
            val req = Request.Builder().url(url).build()
            //3.응답받기 - execute방식은 동기방식임
            //따라서 코루틴의 scope(dispatcher.IO)안에서 실행시켜줘야한다.
            clinet.newCall(req).execute().use { response ->
                val response = response.body()?.string()
                val gson = Gson()
                val holiInfo: HolidayResponse =
                    gson.fromJson(response, HolidayResponse::class.java) //json to gson

                //빨간날 인것만 holidaysList에 넣어줌
                for (i in holiInfo.response.body.items.item) {
                    if (i.isHoliday == "Y")
                        holidaysList.add(i)
                }
            }
        }

    }

    companion object {
        val holidaysList = arrayListOf<HolidayResponse.Response.Body.Items.Item>()
    }
}
