package com.example.calendar.ColoredDate

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import kotlin.collections.ArrayList

class HolidayDecorator(var holidays: ArrayList<CalendarDay>) : DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return holidays.contains(day)
    }

    override fun decorate(view: DayViewFacade?) {
        if (view != null) {
            view.addSpan(ForegroundColorSpan(Color.RED))
        }
    }

}