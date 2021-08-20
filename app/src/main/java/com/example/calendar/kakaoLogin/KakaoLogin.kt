package com.example.calendar.kakaoLogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.example.calendar.MainActivity
import com.example.calendar.R
import com.example.calendar.databinding.ActivityKakaoLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.example.calendar.kakaoLogin.KakaoSDKInit
import com.kakao.sdk.user.UserApiClient
import java.net.URL

class KakaoLogin : AppCompatActivity() {
    private lateinit var binding: ActivityKakaoLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKakaoLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        updateKakaoLoginUi()
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

        //로그아웃 버튼
        binding.btnLogout.setOnClickListener {
            UserApiClient.instance.logout {
                updateKakaoLoginUi()
            }
        }
    }

    private fun checkToken() {
        //토큰정보
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.e("kakaoLogin", "토큰 정보 보기 실패", error)
            } else if (tokenInfo != null) {
                Log.i(
                    "kakaoLogin", "토큰 정보 보기 성공" +
                            "\n회원번호: ${tokenInfo.id}" +
                            "\n만료시간: ${tokenInfo.expiresIn} 초"
                )
            }
        }
    }



    private fun updateKakaoLoginUi() {
        UserApiClient.instance.me { user, error ->
            //로그인이 되어 있을 때
            if (user != null) {
                checkToken()
                startMainActivity(user.kakaoAccount!!.profile!!.thumbnailImageUrl!!)
            } else {
                binding.btnKakaoLogin.visibility = View.VISIBLE
                binding.btnLogout.visibility = View.GONE
                binding.profileImage.setImageResource(R.drawable.ic_calendar)
            }
        }
    }


    private fun startMainActivity(url : String) {
        val intent = Intent(this, MainActivity::class.java)

        intent.apply {
            this.putExtra("profile",url)
        }
        startActivity(intent)
    }
}

