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
            try {
               return dates.value!!.contains(day)
                
            }
            catch (e:Exception){

            }
            return false
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(DotSpan(5F, Color.parseColor("#1D872A")))
        }


}