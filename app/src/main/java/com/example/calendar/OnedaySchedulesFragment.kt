package com.example.calendar

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calendar.Adapter.OnedayDiffUtil
import com.example.calendar.Adapter.RecyclerViewAdapter
import com.example.calendar.Adapter.ScheduleList
import com.example.calendar.BaseActivity.BaseActivity
import com.example.calendar.databinding.ActivityOnedaySchedulesBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.text.SimpleDateFormat

class OnedaySchedulesFragment : Fragment() {
    private lateinit var binding: ActivityOnedaySchedulesBinding
    private lateinit var get_context: Activity
    private lateinit var selectedDate: String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            get_context = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityOnedaySchedulesBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.txtSelectedDate.text =
            "" + CalendarFragment.selctedDate.year + "년 " + (CalendarFragment.selctedDate.month + 1) + "월 " + CalendarFragment.selctedDate.day + "일"





//        for(i in oneDayMutable){
//        Log.e("onedayMutable",i.memo.toString())}

        oneDayMutableList()



            //recyclerview 달기
            adapter = RecyclerViewAdapter(oneDayMutable, LayoutInflater.from(get_context))
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(get_context)






        return view

    }

    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()
    }





    companion object {
        fun newInstance(): Fragment {

            return OnedaySchedulesFragment()
        }

       val oneDayMutable = mutableListOf<ScheduleList>()
        var adapter: RecyclerViewAdapter? = null


        fun oneDayMutableList() {
            //선택한 날짜의 schedulelist만 따로 담은 mutablelist
//        val oneDayMutable = mutableListOf<ScheduleList>()
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")




            for (i in ScheduleList.MutablescheduleList) {
                val start = formatter.parse(i.start)
                val end = formatter.parse(i.end)

                if (CalendarFragment.selctedDate.isInRange(CalendarDay(start), CalendarDay(end))) {
                    oneDayMutable.add(i)
                }
            }
        }

    }



}


