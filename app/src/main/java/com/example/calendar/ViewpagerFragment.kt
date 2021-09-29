package com.example.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.calendar.CalendarFragment.Companion.selctedDate
import com.example.calendar.databinding.ActivityOnedaySchedulesBinding
import com.example.calendar.databinding.ViewpagerfragBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarUtils
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log10

class ViewpagerFragment : Fragment() {
    lateinit var binding: ViewpagerfragBinding
    private val START_POSITION = 50


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ViewpagerfragBinding.inflate(inflater, container, false)
        val view = binding.root


        //어댑터 초기화
        val pagerAdapter = FragmentPagerAdapter(requireActivity())

        //어댑터 붙이기
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.setCurrentItem(START_POSITION, false)


        return view
    }

}


class FragmentPagerAdapter(fragment: FragmentActivity) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 100
    }

    override fun createFragment(position: Int): OnedaySchedulesFragment {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = getItemId(position)
        selctedDate = CalendarDay.from(calendar)
        Log.e("viewpager2","position: "+position+" secltedDate: "+ selctedDate)
        return OnedaySchedulesFragment()
    }

    override fun getItemId(position: Int): Long {

        var selectedDateCalendar = selctedDate.calendar
        var ss = Calendar.getInstance()
        ss.time=selectedDateCalendar.time
        var sdff= position-50
        ss.add(Calendar.DATE, sdff)

        Log.e("chekcckckck","selctedDate.calendar: "+selectedDateCalendar.toString())
        Log.e("viewpager2","position: "+position+" return getItemID: "+selectedDateCalendar.timeInMillis)
        return ss.timeInMillis
    }

    override fun containsItem(itemId: Long): Boolean {
        return itemId is Long
    }
}