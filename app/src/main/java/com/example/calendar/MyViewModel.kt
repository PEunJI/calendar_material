package com.example.calendar

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calendar.Adapter.Schedule
import com.example.calendar.kakaoLogin.KakaoLogin
import com.example.calendar.kakaoLogin.MasterApplication
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashSet

class MyViewModel : ViewModel() {
    private var hashDate = HashSet<CalendarDay>()

    // 내부에서 설정하는 자료형은 private 뮤터블로 & 변수명 앞에 언더바
    // 변경가능하도록 설정
    private lateinit var _calendarDotsAll: MutableLiveData<HashSet<CalendarDay>>


    //외부에서는 언더바없이 변수이름 설정 & mutable이 아닌 그냥 livedata로 값 변경 x & public
    //get할때 mutablelivedata를 반환하도록 설정
    val calendarDotsAll: LiveData<HashSet<CalendarDay>>
        get() = _calendarDotsAll


    private val _oneDayLivedata = MutableLiveData<ArrayList<Schedule>>()
    val oneDayLivedata: LiveData<ArrayList<Schedule>>
        get() = _oneDayLivedata


    //초기값 설정
    init {
        //  _calendarDotsAll.value = 0
    }

    // 뷰모델이 가지고 있는 값을 변경하는 메소드

    /**
     * calendarDotsAll값 변경하는 메소드
     */
    suspend fun getAlldayDots(activity: Activity) {


        val job = CoroutineScope(Dispatchers.IO).async {
            KakaoLogin.response =
                (activity.application as MasterApplication).service.getCalendar()
        }.await()

        //calendarDotsAll 초기화
        //_calendarDotsAll.value?.clear()
        //응답(일정) 받아서 일정 있는 날은 모두 calendarDotsAll에 넣어준다.
        //코루틴 실행 종료되면 dots 찍어줄거임.
        var responses = KakaoLogin.response!!.body()!!
        hashDate.clear()

        for (i in responses.result) {
            rangeDate(
                KakaoLogin.formatter.parse(i.dateStart),
                KakaoLogin.formatter.parse(i.dateEnd)
            )
        }
        Log.e("enrollReset","dot livedata 설정 완료")
        Log.e("enrollReset",""+calendarDotsAll.value?.size)


    }

    //시작 날짜~ 종료 날짜 사이의 모든 날짜들을 calendarDotsAll에 넣어주는 함수
    fun rangeDate(startDate: Date, endDate: Date) {
        val temp_day = java.util.Calendar.getInstance()
        var currentDay = startDate
        //startday가 enddate보다 작을때 동안
        while (currentDay <= endDate) {
            //currentday를 calendarDotsAll에 추가한다
            hashDate.add(CalendarDay(currentDay))
            //currentday의 다음날을 다시 currentday로 초기화해주고 while문반복
            temp_day.time = currentDay
            temp_day.add(java.util.Calendar.DAY_OF_MONTH, 1) //currentday의 다음날
            currentDay = temp_day.time
        }
        _calendarDotsAll = MutableLiveData<HashSet<CalendarDay>>(hashDate)

    }

    /**
    하루 스케줄에 관한 부분 (oneDayMutable)
     */
    fun updateOneDaySchedule(activity: Activity) {
        Schedule.MutablescheduleList.clear()
        _oneDayLivedata.value?.clear()
        //모든 일정을 받아와서 일정 하나하나를 shceduleList객체로 만든 다음 그 객체를 MutablescheduleList(모든스케줄이있는 mutableList)에 추가한다.
        runBlocking {
            withContext(Dispatchers.IO) {
                //모든 일정을 받아와서
                val response =
                    (activity.application as MasterApplication).service.getCalendar()
                var responses = response.body()!!
                //일정 하나하나를 shceduleList객체로 만든다
                for (i in responses.result) {
                    val scheduleList = Schedule()
                    scheduleList.end = i.dateEnd
                    scheduleList.start = i.dateStart
                    val content = i.content
                    val splitString = content.split("@^")
                    //memo는 없을 수도 있으므로 try catch로 실행
                    try {
                        scheduleList.memo = splitString[1]
                    } catch (e: Exception) {
                    }
                    scheduleList.title = splitString[0]
                    scheduleList.id = i.id
                    //그 객체를 MutablescheduleList(모든스케줄이있는 mutableList)에 추가한다.
                    Schedule.MutablescheduleList.add(scheduleList)
                }
            }
            Log.e("oneDayReset", "서버에서 다시 일정 받아오기 완료")

        }
        //MutablescheduleList을 이용해서 선택한 날짜의 일정리스트만 만든다
        getThedayFromAll()
        Log.e("oneDayReset", "선택한 날짜의 일정리스트만 만든다")

//
//        //바뀐 oneDayMutable을 Listadapter에 알려준다
//        recyclerViewAdapter.submitList(oneDayMutable)
    }

    fun getThedayFromAll() {
        //선택한 날짜의 schedulelist만 따로 담은 mutablelist
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")

        //selectedDate(선택한날짜)가 MutablescheduleList(전체 일정)의 시작, 종료 날짜 사이에 있으면 그날의 일정리스트(oneDayMutable)에 추가한다.
        for (i in Schedule.MutablescheduleList) {
            val start = formatter.parse(i.start)
            val end = formatter.parse(i.end)

            if (CalendarFragment.selctedDate.isInRange(CalendarDay(start), CalendarDay(end))) {
                _oneDayLivedata.value!!.add(i)
            }
        }
    }

}

