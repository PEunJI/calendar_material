package com.example.calendar.kakaoLogin

import android.app.Application
import com.example.calendar.api.CalendarService
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.kakao.sdk.common.KakaoSdk
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class KakaoSDKInit : Application() {
    lateinit var service: CalendarService


    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "20bda91f9ba354f86d1df729923ed50a")
        Stetho.initializeWithDefaults(this)




    }

    fun createRetrofit(token : String){
        val header = Interceptor {
            val original = it.request()
            //로그인 되어 있으면
                    val request = original.newBuilder()
                        .header("Authorization", "${token}")
                        .build()
                    it.proceed(request)


        }

        val client = OkHttpClient.Builder()
            .addInterceptor(header)
            .addNetworkInterceptor(StethoInterceptor())
            .build()


        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.kumas.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()


        service = retrofit.create(CalendarService::class.java)
    }


}
