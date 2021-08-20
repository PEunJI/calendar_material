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
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Base64
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.calendar.kakaoLogin.DownloadFilesTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
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


        //캘린더뷰에 날짜별로 색상 다르게 하는 decorator달기
        binding.calendarView.addDecorators(
            SundayDecorator(),
            SaturdayDecorator(),
            TodayDecorator(Context@ this),
            AlldayDecorator()
        )


        /**
         * 툴바
         */
        val profile_image = intent.getStringExtra("profile")
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)   //왼쪽버튼

        //왼쪽 버튼 눌렀을 때
        toolbar.setNavigationOnClickListener {
            //drawer layout
        }

        //왼쪽 버튼에 프로필 사진 넣기
        CoroutineScope(Dispatchers.Main).launch {
            val bitmap =
                withContext(Dispatchers.IO) {
                    DownloadFilesTask.ImageLoader.loadImage(profile_image!!)
                }
            toolbar.setNavigationIcon(bitmap)
        }

    /**
         * 달력 날짜 누르면 프래그먼트 전환
         */
        //달력 날짜 누르면 프래그먼트 전환
        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            var selectedDate: String = "" + date
            selectedDate = selectedDate.substring(11)
            selectedDate = selectedDate.replace("{", "")
            selectedDate = selectedDate.replace("}", "")
            val splitedDate = selectedDate.split("-")

            val year = splitedDate[0]
            val month = (splitedDate[1].toInt() + 1).toString()
            val day = splitedDate[2]

            Log.d("dateTest2", year + month + day)

            val bottomSheet = ScheduleEnrollFragment()
            bottomSheet.apply {
                arguments = Bundle().apply {
                    putString("year", year)
                    putString("month", month)
                    putString("day", day)
                }
            }.show(supportFragmentManager, bottomSheet.tag)
        }

    }

    //커스텀 툴바 적용
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.toobar_menu, menu)
        return true
    }
    //커스텀 툴바 메뉴 눌렀을 때
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.toolbar_today -> Log.d("","")
            R.id.toolbar_schedules -> Log.d("","")
            R.id.toolbar_plus -> Log.d("","")
        }
        return true
    }
}