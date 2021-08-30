package com.example.calendar.BaseActivity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.calendar.*
import com.example.calendar.Adapter.OnedayDiffUtil
import com.example.calendar.Adapter.ScheduleList
import com.example.calendar.databinding.ActivityBaseBinding
import com.example.calendar.kakaoLogin.DownloadFilesTask
import com.example.calendar.kakaoLogin.KakaoLogin
import com.google.android.material.navigation.NavigationView
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.*
import java.util.*

class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var calendarFragment = CalendarFragment()
    private lateinit var binding: ActivityBaseBinding


    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        /**스케줄 가져오기**/
        ScheduleList.getScheules()

        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //초기 프래그먼트 캘린더프래그먼트로 지정
        replaceFragment(calendarFragment, "calendar")


        //툴바사용
        val profile_image = intent.getStringExtra("profile")
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)   //왼쪽버튼

        var drawable: Bitmap?
        //왼쪽 버튼에 프로필 사진 넣기
        CoroutineScope(Dispatchers.Main).launch {
            drawable =
                withContext(Dispatchers.IO) {
                    DownloadFilesTask.ImageLoader.loadImage(profile_image!!)
                }
            drawable = DownloadFilesTask.convertRoundedBitmap(drawable!!)

            toolbar.navigationIcon = BitmapDrawable(drawable)
        }


        /**
         * 네비게이션뷰
         */
        //왼쪽 버튼 눌렀을 때
        toolbar.setNavigationOnClickListener {
            //drawer layout
            binding.drawerLayout.openDrawer(GravityCompat.START)

            val nav_nickname = findViewById<TextView>(R.id.nav_nickname)
            nav_nickname.text = intent.getStringExtra("profile_nickname").toString()
            val nav_profile_img = findViewById<ImageView>(R.id.nav_profile_img)
            Glide.with(this)
                .load(profile_image)
                .circleCrop()
                .into(nav_profile_img)
        }


        binding.navigationView.setNavigationItemSelectedListener(this)
    }

    //스케줄 다시 가져오기
    override fun onRestart() {
        super.onRestart()


    }

    //커스텀 툴바 적용
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.toobar_menu, menu)
        return true
    }

    val calendar = Calendar.getInstance();
    val bottomSheet = ScheduleEnrollFragment()


    //커스텀 툴바 메뉴 눌렀을 때
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbar_today ->

                // binding.calendarView.setDateSelected(calendar.time, true)
                try {
                    //액티비티 화면 재갱신 시키는 코드
                    val intent = intent
                    finish(); //현재 액티비티 종료 실시
                    overridePendingTransition(0, 0); //인텐트 애니메이션 없애기
                    startActivity(intent); //현재 액티비티 재실행 실시
                    overridePendingTransition(0, 0); //인텐트 애니메이션 없애기
                } catch (e: Exception) {
                    e.printStackTrace();
                }
            R.id.toolbar_schedules ->
                replaceFragment(AllSchedulesFragment.newInstance(), "alldays")
               // Log.e("toolbar","toolbar")
            R.id.toolbar_plus ->
                //plus 눌렀을 때 달력 프래그먼트면 오늘 날짜로 일정입력 bottomfragment 띄우
                if (getFragment("calendar") is CalendarFragment) {
                    val bottomSheet = ScheduleEnrollFragment()
                    bottomSheet.apply {
                        arguments = Bundle().apply {
                            putString("year", calendar.get(Calendar.YEAR).toString())
                            putString("month", (calendar.get(Calendar.MONTH) + 1).toString())
                            putString("day", calendar.get(Calendar.DATE).toString())
                        }
                    }.show(supportFragmentManager, bottomSheet.tag)
                }
                //onedayshcedulefragment면, 그날 날짜로 bottomfragment 띄우
                else {
                     val bottomSheet = ScheduleEnrollFragment()
                    bottomSheet.apply {
                        arguments = Bundle().apply {
                            putString("year", CalendarFragment.selctedDate.year.toString())
                            putString("month", (CalendarFragment.selctedDate.month + 1).toString())
                            putString("day", CalendarFragment.selctedDate.day.toString())
                        }
                    }.show(supportFragmentManager, bottomSheet.tag)
                }

        }
        return true
    }

    //네비게이션 메뉴 선택시
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> UserApiClient.instance.logout {
                val intent = Intent(this@BaseActivity, KakaoLogin::class.java)
                startActivity(intent)
            }
        }
        return true
    }


    //fragment 전환 함수
    fun replaceFragment(fragment: Fragment, tag: String) {
        BaseActivity.fragmentManager = supportFragmentManager
        val fragmentTransaction = BaseActivity.fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.frame, fragment, tag).addToBackStack(null).commit()

    }

    //현재 프래그먼트
    fun getFragment(tag: String): Fragment? {
        val fragment = supportFragmentManager.findFragmentByTag(tag)
        return fragment
    }


    companion object {
        var fragmentManager: FragmentManager? = null
    }


}