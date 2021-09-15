package com.example.calendar.kakaoLogin

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.calendar.BaseActivity.BaseActivity
import com.example.calendar.Model.Calendar
import com.example.calendar.R
import com.example.calendar.databinding.ActivityKakaoLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.*
import retrofit2.Response
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*

class KakaoLogin : AppCompatActivity() {
    private lateinit var binding: ActivityKakaoLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKakaoLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        updateKakaoLoginUi()

        //해쉬키
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
            }
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        //


        //로그인 성공/실패 시 콜백
        val callback: ((OAuthToken?, Throwable?) -> Unit) = { token, error ->
            //로그인 실패
            if (error != null) {
                Log.e("kakaoLogin", "Kakao Login Failed :", error)
                //로그인 성공
            } else if (token != null) {
            }
            updateKakaoLoginUi()
        }

        //로그인 버튼
        binding.btnKakaoLogin.setOnClickListener {
            //카카오톡이 깔려있으면 카카오톡으로 로그인
            //로그인 결과(토큰값이 있으면 로그인 성공한것임)는 콜백으로 수행
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@KakaoLogin)) {
                UserApiClient.instance.loginWithKakaoTalk(this@KakaoLogin, callback = callback)
            }//카카오톡이 깔려있지 않으면 웹사이트로 로그인
            else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }

    }


    private fun checkToken() {
        //토큰정보
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.e("kakaoLogin", "토큰 정보 보기 실패", error)
            } else if (tokenInfo != null) {
                KakaoLogin.user_id = tokenInfo.id
                Log.i(
                    "kakaoLogin", "토큰 정보 보기 성공" +
                            "\n회원번호: ${tokenInfo.id}" +
                            "\n만료시간: ${tokenInfo.expiresIn} 초" +
                            "\n만료시간: ${tokenInfo.appId}" +
                            "\nuserId : ${user_id} "
                )
            }
        }
    }


    private fun updateKakaoLoginUi() {
        UserApiClient.instance.me { user, error ->
            //로그인이 되어 있을 때

            if (user != null) {

               val job = CoroutineScope(Dispatchers.Main).async {
                    checkToken()
                }


                val a = CoroutineScope(Dispatchers.IO).async(start = CoroutineStart.LAZY) {

                    response =
                       (application as KakaoSDKInit).service.getCalendar()
                     //  (application as KakaoSDKInit).service.getCalendar("1857817164")
                    var responses = response!!.body()!!
                    for (i in responses.result) {
                        rangeDate(formatter.parse(i.dateStart), formatter.parse(i.dateEnd))
                    }
                }

                a.start()
                Log.e("kakaoLogin","afterUserId: "+ user_id)

                /**점찍기**/
                //응답받아서 날짜만 MutableList에 넣고, decorator에서 그 날짜에 dots를 찍어준다.
                startMainActivity(
                    user.kakaoAccount!!.profile!!.thumbnailImageUrl!!,
                    user.kakaoAccount!!.profile!!.nickname!!
                )
            } else {
                binding.btnKakaoLogin.visibility = View.VISIBLE
                binding.profileImage.setImageResource(R.drawable.ic_calendar)
            }
        }
    }


    private fun startMainActivity(url: String, nickname: String) {
        val intent = Intent(this, BaseActivity::class.java)

        intent.apply {
            this.putExtra("profile", url)
            this.putExtra("profile_nickname", nickname)
        }
        startActivity(intent)
    }


    fun rangeDate(startDate: Date, endDate: Date) {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val c = java.util.Calendar.getInstance()
        var currentDay = startDate
        while (currentDay <= endDate) {
            calendarDotsAll.add(CalendarDay(currentDay))
            c.time = currentDay
            c.add(java.util.Calendar.DAY_OF_MONTH, 1)
            currentDay = c.time
        }
        for (date in calendarDotsAll) {
            Log.e("rangedate", date.toString())
        }

    }

    companion object {
        var user_id = 0L
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
        var response: Response<Calendar>? = null
        val calendarDotsAll: java.util.HashSet<CalendarDay> = java.util.HashSet<CalendarDay>()
    }
}


