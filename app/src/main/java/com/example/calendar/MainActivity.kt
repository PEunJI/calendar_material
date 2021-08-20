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
import android.content.pm.PackageManager

import android.content.pm.PackageInfo
import android.util.Base64
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        //해쉬키 코드로 받기
        try {
            val info = packageManager.getPackageInfo(
                "com.example.calendar",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val str: String = Base64.encodeToString(md.digest(), Base64.DEFAULT)
                Log.d("KeyHash:", str)
                //Toast.makeText(this, str, Toast.LENGTH_LONG).show()
            }
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        //
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