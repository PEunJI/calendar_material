package com.example.calendar.kakaoLogin

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class KakaoSDKInit : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "20bda91f9ba354f86d1df729923ed50a")
    }
}