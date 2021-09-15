package com.example.calendar

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.calendar.BaseActivity.BaseActivity
import com.example.calendar.ColoredDate.*
import com.example.calendar.Model.Calendar
import com.example.calendar.databinding.CalendarFragBinding
import com.example.calendar.kakaoLogin.KakaoLogin
import com.prolificinteractive.materialcalendarview.CalendarDay
import retrofit2.Response
import java.text.SimpleDateFormat


class CalendarFragment : Fragment() {
    private lateinit var binding: CalendarFragBinding
    private lateinit var get_context: Activity
    var response: Response<Calendar>? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            get_context = context

        }
//        /**점찍기**/
//
//        //응답받아서 날짜만 MutableList에 넣고, decorator에서 그 날짜에 dots를 찍어준다.
//        runBlocking {
//            response =
//                (requireActivity().application as KakaoSDKInit).service.getCalendar("${KakaoLogin.user_id}")
//
//            Log.e("job.join", response.toString())
//
//
//                var responses = response!!.body()!!
//                for (i in responses.result) {
//                    rangeDate(formatter.parse(i.dateStart), formatter.parse(i.dateEnd))
//                }
//
//
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = CalendarFragBinding.inflate(inflater, container, false)
        val view = binding.root


        //캘린더뷰에 날짜별로 색상 다르게 하는 decorator달기
        binding.calendarView.addDecorators(
            EventDecorator(KakaoLogin.calendarDotsAll),
            SundayDecorator(),
            SaturdayDecorator(),
            TodayDecorator(get_context),
            AlldayDecorator()
        )

        Log.e("calendarDogs",KakaoLogin.calendarDotsAll.size.toString())



        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            selctedDate = date

            (activity as BaseActivity).replaceFragment(
                OnedaySchedulesFragment.newInstance(),
                "schedules"
            )

        }



        return view

    }

    override fun onResume() {
        super.onResume()
        Log.e("fraggggcheck", "onResume")

    }

    override fun onPause() {
        super.onPause()
        Log.e("fraggggcheck", "onPause")

    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("fraggggcheck", "onDestroy")

    }

    companion object {
        fun newInstance(): Fragment {

            return CalendarFragment()
        }

        var selctedDate = CalendarDay.today()

    }

    //캘린더에 점찍기 위해서 스케줄 있는 날짜만 받아움
    // val calendarDots: MutableList<CalendarDay> = mutableListOf<CalendarDay>() //처음 끝 날짜만
    //  val calendarDotsAll: HashSet<CalendarDay> = HashSet<CalendarDay>() //처음~끝 날짜 모두


    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")


//    fun rangeDate(startDate: Date, endDate: Date) {
//        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
//        val c = java.util.Calendar.getInstance()
//        var currentDay = startDate
//        while (currentDay <= endDate) {
//            calendarDotsAll.add(CalendarDay(currentDay))
//            c.time = currentDay
//            c.add(java.util.Calendar.DAY_OF_MONTH, 1)
//            currentDay = c.time
//        }
//        for (date in calendarDotsAll) {
//            Log.e("rangedate", date.toString())
//        }
//    }

}







