package com.example.calendar.kakaoLogin

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class DownloadFilesTask {

    object ImageLoader {
       suspend fun loadImage(imageUrl: String): Drawable? {
            val bmp: Bitmap? = null
            try {
                val url = URL(imageUrl)
                val stream = url.openStream()
                val bitmap = BitmapFactory.decodeStream(stream)
                return BitmapDrawable(bitmap)
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return BitmapDrawable(bmp)
        }
    }


}