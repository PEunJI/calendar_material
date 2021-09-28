package com.example.calendar

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.calendar.Adapter.LinearLayoutManagerWrapper
import com.example.calendar.Adapter.RecyclerViewAdapter
import com.example.calendar.CalendarFragment.Companion.selctedDate
import com.example.calendar.databinding.ActivityOnedaySchedulesBinding
import com.example.calendar.kakaoLogin.KakaoLogin.Companion.myViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.text.SimpleDateFormat
import java.util.*

class OnedaySchedulesFragment : Fragment() {
    private lateinit var binding: ActivityOnedaySchedulesBinding
    private lateinit var get_context: Activity
    private lateinit var selectedDate: String
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
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
        binding = ActivityOnedaySchedulesBinding.inflate(inflater, container, false)
        val view = binding.root




        //calednarFragment에서 보낸 데이터 받기 
        var holidayTitle: String? = ""
        if (arguments != null) {
            holidayTitle = arguments?.getString("holidayTitle", "")
        }
        //제목(클릭한 날짜)달기
        binding.txtSelectedDate.text =
            "" + CalendarFragment.selctedDate.year + "년 " + (CalendarFragment.selctedDate.month + 1) + "월 " + CalendarFragment.selctedDate.day + "일 " + "${holidayTitle}"




        myViewModel.updateOneDaySchedule(requireActivity())


        //recyclerview 달기
        recyclerViewAdapter = RecyclerViewAdapter(requireActivity().application, requireActivity())
        binding.recyclerView.adapter = recyclerViewAdapter
        binding.recyclerView.layoutManager = LinearLayoutManagerWrapper(get_context)


        //3. 뷰모델(onedaylivedata)에 값이 변경될 때 할 행동
        //바뀐 oneDayLivedata을 Listadapter에 알려준다
        //it.size가 1개 이상 일 때는 submitlist로 잘 작동이 되는데
        //it.size가 0개(즉, 스케줄을 다 지울 때)는 submitlist로 작동이 안된다.
        //아마 콜백 함수에 들어가는 매개변수 schedule이 empty인데 먼가 여기서 에러가 나지는 않고 그냥 작동이 되는듯
        //그러니까 return oldItem.id == newItem.id <<여기에서 비교를 할 대상이 없어서
        //ui업뎃이 안되는거 같다.
        //그래서 it==empty 일 때는 그냥 notifyItemRemoved을 넣어줬다.
        myViewModel.oneDayLivedata.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                recyclerViewAdapter.notifyItemRemoved(0)
                //하나 남은 아이템을 삭제 하는 것이므로
                //항상 맨 첫번째 아이템이 삭제됨 . position에 0을 넣어준다.
            } else recyclerViewAdapter.submitList(it)


        })


        return view

    }

    //일정을 편집할 때 마다 서버에서 새로 데이터를 받아와서 listadpater에게 알려줌
    override fun onResume() {
        super.onResume()

    }


    companion object {
        fun newInstance(): Fragment {

            return OnedaySchedulesFragment()
        }

    }


}


