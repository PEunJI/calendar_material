package com.example.calendar.kakaoLogin

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.calendar.BaseActivity.BaseActivity
import com.example.calendar.Model.Calendar
import com.example.calendar.MyViewModel
import com.example.calendar.R
import com.example.calendar.databinding.ActivityKakaoLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
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
        myViewModel = ViewModelProvider(this)[MyViewModel::class.java]
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


    val checkTokenJob =

        CoroutineScope(Dispatchers.Main).async {
            //토큰정보가져와서 user_id 변수에 저장
            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                if (error != null) {
                    Log.e("kakaoLogin", "토큰 정보 보기 실패", error)
                } else if (tokenInfo != null) {
                    user_id = tokenInfo.id
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
                /**점찍기**/
                //로그인이 되어 있으면 retrofit service를 갱신?새로만든?다.
                (application as MasterApplication).createRetrofit(user_id)
                runBlocking {

                    //checkToken작업이 완료된 후에 service.getCalendar작업을 한다
                    checkTokenJob.join()
                    myViewModel.getAlldayDots(this@KakaoLogin)

                }

                startMainActivity(
                    user.kakaoAccount!!.profile!!.thumbnailImageUrl!!,
                    user.kakaoAccount!!.profile!!.nickname!!
                )
            } //로그인 실패 시
            else {
                binding.btnKakaoLogin.visibility = View.VISIBLE
                binding.profileImage.setImageResource(R.drawable.ic_calendar)
            }
        }
    }


    //로그인 됐으면 BaseActivity로 이동
    private fun startMainActivity(url: String, nickname: String) {
        val intent = Intent(this, BaseActivity::class.java)

        intent.apply {
            this.putExtra("profile", url)
            this.putExtra("profile_nickname", nickname)
        }
        startActivity(intent)
    }


    companion object {
        var user_id = 0L
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
        var response: Response<Calendar>? = null

        //   val calendarDotsAll =MutableLiveData<HashSet<CalendarDay>>()
        lateinit var myViewModel: MyViewModel
    }
}


