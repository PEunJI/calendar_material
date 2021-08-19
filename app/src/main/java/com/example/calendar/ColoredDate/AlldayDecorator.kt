package com.example.calendar.ColoredDate

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.SizeF
import com.example.calendar.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class AlldayDecorator : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return true
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(RelativeSizeSpan(1.3f))
        view.addSpan(StyleSpan(Typeface.BOLD))
    }

}