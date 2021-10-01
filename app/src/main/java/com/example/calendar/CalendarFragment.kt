package com.example.calendar

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.calendar.BaseActivity.BaseActivity
import com.example.calendar.ColoredDate.*
import com.example.calendar.Dots.GetHolidays.Companion.holidaysList

import com.example.calendar.databinding.CalendarFragBinding
import com.example.calendar.kakaoLogin.KakaoLogin.Companion.holidayDateList
import com.example.calendar.kakaoLogin.KakaoLogin.Companion.myViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.runBlocking


class CalendarFragment : Fragment() {
    private lateinit var binding: CalendarFragBinding
    private lateinit var get_context: Activity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            get_context = context

        }
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
            AlldayDecorator(),
            EventDecorator(myViewModel.calendarDotsAll),
            SundayDecorator(),
            SaturdayDecorator(),
            TodayDecorator(get_context),
            HolidayDecorator(holidayDateList)
        )


        //캘린더뷰의 날짜가 선택되면 그날의 일정을 보여주는 프래그먼트로 이동
        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            selctedDate = date
            realSelectedDate = date
            val bundle = Bundle()


            //만약 선택된 날이 공휴일이면 공휴일이름을 onedayscheduleFragment에 보낸다.
            for (i in holidaysList) {
                var holidayDate = i.locdate.toString()
                var year = holidayDate.substring(0, 4).toInt()
                var month = holidayDate.substring(4, 6).toInt() - 1
                var day = holidayDate.substring(6).toInt()
                if (selctedDate == CalendarDay(year, month, day)) {
                    bundle.putString("holidayTitle", "(${i.dateName})")
                }
            }

//            (activity as BaseActivity).replaceFragment(
//                OnedaySchedulesFragment.newInstance(),
//                "schedules",
//                bundle
//            )




            (activity as BaseActivity).replaceFragment(
                ViewpagerFragment(),
                "scheduless"
            )


        }



        return view

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 뷰모델에 값이 변경될 때 할 행동
        myViewModel.calendarDotsAll.observe(viewLifecycleOwner, Observer {
            Log.e("enrollReset", "점찍기observe")
            binding.calendarView.addDecorators(
                EventDecorator(myViewModel.calendarDotsAll)
            )
        })
    }


    companion object {
        fun newInstance(): Fragment {

            return CalendarFragment()
        }

        var selctedDate = CalendarDay.today()
        var realSelectedDate =  CalendarDay.today()
    }

}







