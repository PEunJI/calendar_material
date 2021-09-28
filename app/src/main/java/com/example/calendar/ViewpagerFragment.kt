package com.example.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.calendar.databinding.ActivityOnedaySchedulesBinding
import com.example.calendar.databinding.ViewpagerfragBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarUtils
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ViewpagerFragment : Fragment() {
    lateinit var binding : ViewpagerfragBinding
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
    //프래그먼트들의 배열을 만들어서 관리함

     override fun getItemCount(): Int {
        return 100
     }

     //배열에 있는 position번째 프래그먼트를 만들어서 뷰에 보여주는듯.
    override fun createFragment(position: Int): OnedaySchedulesFragment {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = getItemId(position)
        val LongtoString = formatter.format(calendar.time)
        val StringtoDate = formatter.parse(LongtoString)
        val cal = Calendar.getInstance()
        cal.time = StringtoDate
        CalendarFragment.selctedDate = CalendarDay.from(cal)

        return OnedaySchedulesFragment()

    }

    override fun getItemId(position: Int): Long {
        //선택된날에 하루 더해주는 부분
        //selectedDate(Calendarday).calendar(to java.Calendar)
        var b = Date()
        var a : Calendar = CalendarFragment.selctedDate.calendar

        b.time=a.timeInMillis

        val c = Calendar.getInstance()
        c.time=b
        c.add(Calendar.DATE,position-50)


        return c.timeInMillis
    }

    override fun containsItem(itemId: Long): Boolean {
        return itemId is Long
    }
}