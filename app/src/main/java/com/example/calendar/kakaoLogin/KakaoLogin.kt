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
                updateKakaoLoginUi()
                //로그인 성공
            } else if (token != null) {
                updateKakaoLoginUi()
                binding.profileImage.setOnClickListener {
                    startMainActivity()
                }
            }
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
                binding.btnKakaoLogin.visibility = View.GONE
                binding.btnLogout.visibility = View.VISIBLE

                Log.d("kakaoLogin", "invoke: id =" + user.id)
                Log.d("kakaoLogin", "invoke: email =" + user.kakaoAccount!!.email)
                Log.d("kakaoLogin", "invoke: nickname =" + user.kakaoAccount!!.profile!!.nickname)
                Log.d("kakaoLogin", "invoke: gender =" + user.kakaoAccount!!.gender)
                Log.d("kakaoLogin", "invoke: ageRange =" + user.kakaoAccount!!.ageRange)

                binding.nickname.text = user.kakaoAccount!!.profile!!.nickname
                //글라이드를 이용하여 카카오프로필사진 url 을 이미지뷰에 넣는다.
                Glide.with(binding.profileImage)
                    .load(user.kakaoAccount!!.profile!!.thumbnailImageUrl).circleCrop()
                    .into(binding.profileImage)
                Log.d("profile", user.kakaoAccount!!.profile!!.thumbnailImageUrl.toString())
            } else {
                binding.btnKakaoLogin.visibility = View.VISIBLE
                binding.btnLogout.visibility = View.GONE
                binding.nickname.text = null
                binding.profileImage.setImageDrawable(null)
            }
        }
    }


    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}

