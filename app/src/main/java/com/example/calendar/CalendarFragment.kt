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
import com.example.calendar.databinding.CalendarFragBinding
import com.example.calendar.kakaoLogin.KakaoLogin.Companion.myViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay


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


        // 뷰모델에 값이 변경될 때 할 행동
        myViewModel.calendarDotsAll.observe(viewLifecycleOwner, Observer {
            binding.calendarView.addDecorators(
                EventDecorator(myViewModel.calendarDotsAll)
            )
        })


        Log.e("flowcheckk","캘린더프래그먼트 전환완료 ")


        //캘린더뷰에 날짜별로 색상 다르게 하는 decorator달기
        binding.calendarView.addDecorators(
          //  EventDecorator(myViewModel.calendarDotsAll),
            SundayDecorator(),
            SaturdayDecorator(),
            TodayDecorator(get_context),
            AlldayDecorator()
        )

        //캘린더뷰의 날짜가 선택되면 그날의 일정을 보여주는 프래그먼트로 이동
        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            selctedDate = date

            (activity as BaseActivity).replaceFragment(
                OnedaySchedulesFragment.newInstance(),
                "schedules"
            )
        }

        return view

    }

    companion object {
        fun newInstance(): Fragment {

            return CalendarFragment()
        }

        var selctedDate = CalendarDay.today()

    }

}







