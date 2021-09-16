package com.example.calendar

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calendar.Adapter.RecyclerViewAdapter
import com.example.calendar.databinding.ActivityOnedaySchedulesBinding
import com.example.calendar.kakaoLogin.KakaoLogin.Companion.myViewModel

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
        //제목(클릭한 날짜)달기
        binding.txtSelectedDate.text =
            "" + CalendarFragment.selctedDate.year + "년 " + (CalendarFragment.selctedDate.month + 1) + "월 " + CalendarFragment.selctedDate.day + "일"

        myViewModel.updateOneDaySchedule(requireActivity())



        //recyclerview 달기
        recyclerViewAdapter = RecyclerViewAdapter(requireActivity().application)
        binding.recyclerView.adapter = recyclerViewAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(get_context)


        //3. 뷰모델(onedaylivedata)에 값이 변경될 때 할 행동
        //바뀐 oneDayLivedata을 Listadapter에 알려준다
        myViewModel.oneDayLivedata.observe(viewLifecycleOwner, Observer {
            recyclerViewAdapter.submitList(it)
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


