package com.example.calendar

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.calendar.ColoredDate.AlldayDecorator
import com.example.calendar.ColoredDate.SaturdayDecorator
import com.example.calendar.ColoredDate.SundayDecorator
import com.example.calendar.ColoredDate.TodayDecorator
import android.content.pm.PackageManager

import android.content.pm.PackageInfo
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.calendar.BaseActivity.BaseActivity
import com.example.calendar.databinding.CalendarFragBinding
import com.example.calendar.kakaoLogin.DownloadFilesTask
import com.example.calendar.kakaoLogin.KakaoLogin
import com.google.android.material.navigation.NavigationView
import com.kakao.sdk.user.UserApiClient
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


class CalendarFragment : Fragment() {
    private lateinit var binding: CalendarFragBinding
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


        binding = CalendarFragBinding.inflate(inflater, container, false)
        val view = binding.root


        //캘린더뷰에 날짜별로 색상 다르게 하는 decorator달기
        binding.calendarView.addDecorators(
            SundayDecorator(),
            SaturdayDecorator(),
            TodayDecorator(get_context),
            AlldayDecorator()
        )



        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            selctedDate = date

            (activity as BaseActivity).replaceFragment(
                OnedaySchedulesFragment.newInstance(),
                "schedules"
            )

        }

        return view

    }

    override fun onResume() {
        super.onResume()
    }


    companion object {
        fun newInstance(): Fragment {

            return CalendarFragment()
        }

        var selctedDate = CalendarDay.today()
    }
}







