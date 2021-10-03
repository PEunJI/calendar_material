package com.example.calendar

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.calendar.BaseActivity.BaseActivity

class AlarmRecevier : BroadcastReceiver() {
    lateinit var notificationManager: NotificationManager
    lateinit var builder: NotificationCompat.Builder

    val CHANNEL_ID = "channelID"
    val CHANNEL_NAME = "알림채널 이름"
    val CHANNEL_DESCRITION = "알림채널 설명"
    val notification_ID = 45

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("alarm","onreceive")
        notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRITION
            }
            // Register the channel with the system
            notificationManager.createNotificationChannel(channel)
            builder = NotificationCompat.Builder(context, CHANNEL_ID)
        } else {
            builder = NotificationCompat.Builder(context)
        }

        val intent = Intent(context, BaseActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(context, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT)


        //노티 설정
        val notification = builder
            .setSmallIcon(android.R.drawable.ic_menu_my_calendar)
            .setContentTitle("title") //제목
            .setContentText("content") //내용
            .setAutoCancel(true) //알림 클릭 시 삭
            .setShowWhen(true) //알림 온 시간
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent) //노티 클릭 시 인텐트작업
            .build()
        //알림 표시
        notificationManager.notify(notification_ID, notification)


    }
}