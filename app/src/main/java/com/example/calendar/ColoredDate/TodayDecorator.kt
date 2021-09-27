package com.example.calendar.ColoredDate

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.style.StyleSpan
import com.example.calendar.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

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