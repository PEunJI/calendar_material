package com.example.calendar.ColoredDate

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.*
import android.graphics.drawable.Drawable
import com.prolificinteractive.materialcalendarview.MaterialCalendarView

import android.text.style.RelativeSizeSpan

import android.graphics.Typeface

import android.text.style.StyleSpan
import com.example.calendar.R

class TodayDecorator(var context: Context) : DayViewDecorator {
    private var date: CalendarDay? = null

    fun OneDayDecorator() {
        date = CalendarDay.today()
    }

    override fun shouldDecorate(day: CalendarDay): Boolean {
        OneDayDecorator()
        return date != null && day == date
    }

    override fun decorate(view: DayViewFacade) {
        val drawable : Drawable? = context.getDrawable(R.drawable.todaybgr)
        view.addSpan(StyleSpan(Typeface.BOLD))
        view.setBackgroundDrawable(drawable!!)
    }

}