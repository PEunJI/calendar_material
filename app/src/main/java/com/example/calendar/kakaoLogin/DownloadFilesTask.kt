package com.example.calendar.kakaoLogin

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


  class DownloadFilesTask {
        //string(url) to bitmap
    object ImageLoader {
       suspend fun loadImage(imageUrl: String): Bitmap? {
            val bmp: Bitmap? = null
            try {
                val url = URL(imageUrl)
                val stream = url.openStream()
                var bitmap = BitmapFactory.decodeStream(stream)
                return bitmap
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return bmp
        }
    }
        //make circle bitmap
   companion object {
       fun convertRoundedBitmap(bitmap: Bitmap): Bitmap? {
           val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
           val canvas = Canvas(output)
           val color = -0xbdbdbe
           val paint = Paint()
           val rect = Rect(0, 0, bitmap.width, bitmap.height)
           paint.setAntiAlias(true)
           canvas.drawARGB(0, 0, 0, 0)
           paint.setColor(color)
           val size = (bitmap.width / 2).toFloat()
           canvas.drawCircle(size, size, size, paint)
           paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
           canvas.drawBitmap(bitmap, rect, rect, paint)
           bitmap.recycle()
           return output
       }
   }
}