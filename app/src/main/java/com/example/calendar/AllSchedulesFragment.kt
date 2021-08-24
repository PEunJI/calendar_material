package com.example.calendar

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calendar.Adapter.RecyclerViewAdapter
import com.example.calendar.Adapter.ScheduleList
import com.example.calendar.databinding.ActivityOnedaySchedulesBinding
import com.example.calendar.databinding.FragmentAllSchedulesBinding

class AllSchedulesFragment : Fragment() {
    private lateinit var binding: FragmentAllSchedulesBinding
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
        binding = FragmentAllSchedulesBinding.inflate(inflater, container, false)
        val view = binding.root

        val adapter =
            RecyclerViewAdapter(ScheduleList.MutablescheduleList, LayoutInflater.from(get_context))
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(get_context)
        return view
    }
    companion object {
        fun newInstance(): Fragment {

            return CalendarFragment()
        }
    }
}

