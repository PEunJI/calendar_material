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
import com.example.calendar.Adapter.LinearLayoutManagerWrapper
import com.example.calendar.Adapter.RecyclerViewAdapter
import com.example.calendar.Adapter.Schedule
import com.example.calendar.databinding.FragmentAllSchedulesBinding
import com.example.calendar.kakaoLogin.KakaoLogin
import com.example.calendar.kakaoLogin.KakaoLogin.Companion.myViewModel

class AllSchedulesFragment : Fragment() {
    private lateinit var binding: FragmentAllSchedulesBinding
    private lateinit var get_context: Activity
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
        binding = FragmentAllSchedulesBinding.inflate(inflater, container, false)
        val view = binding.root

        myViewModel.updateOneDaySchedule(requireActivity())


        //recyclerview 달기
        recyclerViewAdapter =
            RecyclerViewAdapter(requireActivity().application, requireActivity())
        binding.recyclerView.adapter = recyclerViewAdapter
        binding.recyclerView.layoutManager = LinearLayoutManagerWrapper(get_context)

        //allschedule이 바뀌면
        KakaoLogin.myViewModel.allScheduls.observe(viewLifecycleOwner, Observer {
//            if (it.isNullOrEmpty()) {
//                recyclerViewAdapter.notifyItemRemoved(0)
//                //하나 남은 아이템을 삭제 하는 것이므로
//                //항상 맨 첫번째 아이템이 삭제됨 . position에 0을 넣어준다.
//            } else {
                recyclerViewAdapter.submitList(it.toMutableList())
//            }

            })


        return view

    }

    override fun onResume() {
        super.onResume()
    }

    companion object {
        fun newInstance(): Fragment {

            return AllSchedulesFragment()
        }
    }
}

