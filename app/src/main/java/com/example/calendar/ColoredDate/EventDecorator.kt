package com.example.calendar.ColoredDate

import android.graphics.Color
import com.example.calendar.Dots.Dots
import com.prolificinteractive.materialcalendarview.spans.DotSpan

import com.prolificinteractive.materialcalendarview.DayViewFacade

import com.prolificinteractive.materialcalendarview.CalendarDay

import com.prolificinteractive.materialcalendarview.DayViewDecorator


class EventDecorator(var dates: Collection<CalendarDay>): DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(5F, Color.parseColor("#1D872A")))
    }
}