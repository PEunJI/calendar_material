package com.example.calendar

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.example.calendar.databinding.ActivityOnedaySchedulesBinding

class OnedaySchedulesFragment : Fragment() {
    private lateinit var binding: ActivityOnedaySchedulesBinding
    private lateinit var get_context: Activity
    private lateinit var selectedDate : String
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            get_context = context
        }
    }

    //달력 fragment에서 받은 날짜 데이터 받
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("give_date"){key, bundle ->
            bundle.getString("date")?.let{
                selectedDate = it
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityOnedaySchedulesBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.txtSelectedDate.text = selectedDate
        return view

    }


    companion object {
        fun newInstance(): Fragment {

            return OnedaySchedulesFragment()
        }
    }
}