package com.example.calendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.calendar.ColoredDate.SaturdayDecorator
import com.example.calendar.ColoredDate.SundayDecorator
import com.example.calendar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.calendarView.addDecorators(
            SundayDecorator(),
            SaturdayDecorator()
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

            Toast.makeText(this@MainActivity,""+date
                ,Toast.LENGTH_SHORT).show()
            Log.d("clickdate->", "date->$month$splitedDate")
        }

    }



}