package com.example.calendar.BaseActivity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.example.calendar.*
import com.example.calendar.Adapter.Schedule
import com.example.calendar.databinding.ActivityBaseBinding
import com.example.calendar.kakaoLogin.DownloadFilesTask
import com.example.calendar.kakaoLogin.KakaoLogin
import com.example.calendar.kakaoLogin.KakaoLogin.Companion.myViewModel
import com.google.android.material.navigation.NavigationView
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var calendarFragment = CalendarFragment()
    private lateinit var binding: ActivityBaseBinding
    lateinit var alarmManager: AlarmManager
    val today = Calendar.getInstance().time
    val scheduleTimeArray = mutableListOf<Date>()

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //초기 프래그먼트 캘린더프래그먼트로 지정
        replaceFragment(calendarFragment, "calendar")

        //allSchedules가 바뀔때마다 알림 새로 설정
        myViewModel.allScheduls.observe(this, androidx.lifecycle.Observer {
            setAlarm()
            Log.e("alarm", "observer come")
        })

        /**
         * 툴바사용
         */
        val profile_image = intent.getStringExtra("profile")
        Log.e("profile_image", profile_image.toString())
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)   //왼쪽버튼

        var drawable: Bitmap?
        try {
            //왼쪽 버튼에 프로필 사진 넣기
            CoroutineScope(Dispatchers.Main).launch {
                drawable =
                    withContext(Dispatchers.IO) {
                        DownloadFilesTask.ImageLoader.loadImage(profile_image!!)
                    }
                drawable = DownloadFilesTask.convertRoundedBitmap(drawable!!)

                toolbar.navigationIcon = BitmapDrawable(drawable)
            }
        } catch (e: java.lang.Exception) {
        }

        /**
         * 네비게이션뷰
         */
        //왼쪽 버튼 눌렀을 때
        toolbar.setNavigationOnClickListener()
        {
            //drawer layout
            binding.drawerLayout.openDrawer(GravityCompat.START)

            //이름이랑 프사 넣기
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
        var thisFragment = supportFragmentManager.findFragmentById(R.id.frame)

        when (item.itemId) {
            //새로고침 버튼(오늘날짜에 원이 찍힌 캘린더로이동)
            R.id.toolbar_today ->
                // binding.calendarView.setDateSelected(calendar.time, true)
                try {
                    //액티비티 화면 재갱신 시키는 코드
                    val intent = intent
                    finish() //현재 액티비티 종료 실시
                    overridePendingTransition(0, 0); //인텐트 애니메이션 없애기
                    startActivity(intent) //현재 액티비티 재실행 실시
                    overridePendingTransition(0, 0) //인텐트 애니메이션 없애기
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            //allSchedules 버튼
            R.id.toolbar_schedules ->
                replaceFragment(AllSchedulesFragment.newInstance(), "alldays")
            // Log.e("toolbar","toolbar")
            //+버튼
            R.id.toolbar_plus ->
                //plus 눌렀을 때 달력 프래그먼트면 오늘 날짜로 일정입력 bottomfragment 띄우
                //  if (getFragment("calendar") is CalendarFragment) {
                if (thisFragment is CalendarFragment) {
                    val bottomSheet = ScheduleEnrollFragment()
                    bottomSheet.apply {
                        arguments = Bundle().apply {
                            putString("year", calendar.get(Calendar.YEAR).toString())
                            putString("month", (calendar.get(Calendar.MONTH) + 1).toString())
                            putString("day", calendar.get(Calendar.DATE).toString())
                        }
                    }.show(supportFragmentManager, bottomSheet.tag)
                    Log.e(
                        "datecheckeeeee",
                        "calendar come " + calendar.get(Calendar.DATE).toString()
                    )
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
                        Log.e(
                            "datecheckeeeee",
                            "else come " + CalendarFragment.selctedDate.day.toString()
                        )

                    }.show(supportFragmentManager, bottomSheet.tag)
                }

        }
        return true
    }

    //네비게이션 메뉴 선택시
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            //로그아웃 클릭시 로그아웃 하고 로그인 액티비티로 이동
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

    fun replaceFragment(fragment: Fragment, tag: String, bundle: Bundle) {
        BaseActivity.fragmentManager = supportFragmentManager
        fragment.arguments = bundle
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

    //알람 설정
    fun setAlarm() {
        alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        setStartDateWithToday()


        //다음 일정의 인덱스 찾기
        var nextScheduleIndex = (scheduleTimeArray.indexOf(today)) + 1
        try {
            val nextScheduleDate = scheduleTimeArray[nextScheduleIndex] //다음일
            val intentToReceiver = Intent(this, AlarmRecevier::class.java)

            putIntent(nextScheduleDate,intentToReceiver)

            val pendingIntent =
                PendingIntent.getBroadcast(this, 101, intentToReceiver, PendingIntent.FLAG_UPDATE_CURRENT)
            val receiveTime = Calendar.getInstance()
            receiveTime.time = nextScheduleDate
            receiveTime.add(Calendar.DATE, -1) //다음 일정의 하루 전날
            receiveTime.set(Calendar.HOUR_OF_DAY, 9) //9시 0분
            receiveTime.set(Calendar.MINUTE, 0)

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, receiveTime.timeInMillis, pendingIntent)
            Log.e("alarm","nextScheduleDate"+nextScheduleDate.toString())
            Log.e("alarm", receiveTime.time.toString())
            Log.e("alarm", scheduleTimeArray.indexOf(today).toString())
            Log.e("alarm", nextScheduleIndex.toString())

        } catch (e: java.lang.Exception) {
            Log.e("alarm", "catch")

        }
    }

    fun setStartDateWithToday() {
        //start날짜만 가져오기 + 오늘 날짜 + 정렬
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        //MutablescheduleList(모든 schedule객체가 들어있는 livedata)에서 start날짜만 scheduleTimeArray에 넣는다.
        for (i in Schedule.MutablescheduleList) {
            val cal = Calendar.getInstance()
            cal.time = formatter.parse(i.start)
            scheduleTimeArray.add(cal.time)
        }
        //오늘 날짜를 넣고 정렬한다
        scheduleTimeArray.add(today)
        scheduleTimeArray.sort()
        //
    }

    fun putIntent(nextScheduleDate: Date, intent: Intent) {
        //제목과 날짜를 찾아서 intent에 보낸다
        for (i in Schedule.MutablescheduleList) {
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val date = formatter.parse(i.start)
            if (date == nextScheduleDate) {
                val title = "title: " + i.title!!
                val alarmDate = i.start + " ~ " + i.end

                intent.apply {
                    this.putExtra("title", title)
                    this.putExtra("alarmDate", alarmDate)
                }
                Log.e("alarm", title+alarmDate)
            }
        }
    }


}