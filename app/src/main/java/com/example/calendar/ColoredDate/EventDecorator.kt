package com.example.calendar.ColoredDate

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan


class EventDecorator(var dates: LiveData<HashSet<CalendarDay>>): DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay): Boolean {
        Log.e("calendarDots",dates.toString())
        for ( i in dates.value!!.iterator()) {
            Log.e("initt", "dates: " + i)
        }
        return dates.value!!.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(DotSpan(5F, Color.parseColor("#1D872A")))
    }
}