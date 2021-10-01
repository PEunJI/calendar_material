package com.example.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.calendar.CalendarFragment.Companion.realSelectedDate
import com.example.calendar.CalendarFragment.Companion.selctedDate
import com.example.calendar.ViewpagerFragment.Companion.TenonedaySchedulesFrags
import com.example.calendar.databinding.ViewpagerfragBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.*

class ViewpagerFragment : Fragment() {
    lateinit var binding: ViewpagerfragBinding
    private val START_POSITION = 5
    private var pagerAdapter: FragmentPagerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ViewpagerfragBinding.inflate(inflater, container, false)
        val view = binding.root

        //어댑터 초기화
        pagerAdapter = FragmentPagerAdapter(requireActivity()).apply {
            makeScheduleFragsArray()
        }

        Log.e("selctedDate", "viewpager2 adapter Attached")
        //어댑터 붙이기
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.setCurrentItem(START_POSITION, false)
       // binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL


        return view

    }

    companion object {
        val TenonedaySchedulesFrags: ArrayList<OnedaySchedulesFragment> = arrayListOf()


    }
}

class FragmentPagerAdapter(fragment: FragmentActivity) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return TenonedaySchedulesFrags.size
    }

    override fun createFragment(position: Int): Fragment {
        val selectedDateCalendar = realSelectedDate.calendar
        val tempCalendar = Calendar.getInstance()
        tempCalendar.time = selectedDateCalendar.time
        tempCalendar.add(Calendar.DATE, (position - 5))
        selctedDate = CalendarDay.from(tempCalendar)
        Log.e("selctedDate", "position" + position.toString())

        return TenonedaySchedulesFrags[position]
    }

    fun makeScheduleFragsArray() {
        TenonedaySchedulesFrags.clear()
        for (i in -5..5) {
            val selectedDateCalendar = realSelectedDate.calendar
            val tempCalendar = Calendar.getInstance()
            tempCalendar.time = selectedDateCalendar.time
            tempCalendar.add(Calendar.DATE, i)
            selctedDate = CalendarDay.from(tempCalendar)
            Log.e("selctedDate", "1. selctedDate: " + selctedDate.toString())
            TenonedaySchedulesFrags.add(OnedaySchedulesFragment())
            notifyItemInserted(TenonedaySchedulesFrags.size - 1)
        }

    }

}