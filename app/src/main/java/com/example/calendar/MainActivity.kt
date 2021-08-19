package com.example.calendar

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.calendar.ColoredDate.AlldayDecorator
import com.example.calendar.ColoredDate.SaturdayDecorator
import com.example.calendar.ColoredDate.SundayDecorator
import com.example.calendar.ColoredDate.TodayDecorator
import com.example.calendar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.calendarView.addDecorators(
            SundayDecorator(),
            SaturdayDecorator(),
            TodayDecorator(Context@this),
            AlldayDecorator()
        )


        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            var selectedDate :String = ""+date
            selectedDate = selectedDate.substring(11)
            selectedDate = selectedDate.replace("{","")
            selectedDate = selectedDate.replace("}","")
            val  splitedDate = selectedDate.split("-")

            val year = splitedDate[0]
            val month = (splitedDate[1].toInt() +1).toString()
            val day = splitedDate[2]

            Log.d("dateTest2",year+month+day)

            val bottomSheet = ScheduleEnrollFragment()
            bottomSheet.apply {
                arguments = Bundle().apply{
                    putString("year", year)
                    putString("month", month)
                    putString("day", day)
                }
            }.show(supportFragmentManager,bottomSheet.tag)
        }

    }



}