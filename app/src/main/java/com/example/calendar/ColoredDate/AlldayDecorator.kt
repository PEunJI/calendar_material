package com.example.calendar.ColoredDate

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.SizeF
import com.example.calendar.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import android.text.style.ForegroundColorSpan

class AlldayDecorator : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return true
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(RelativeSizeSpan(1.3f))
        //view.addSpan(StyleSpan(Typeface.BOLD))
        //나중에 다크모드/라이트모드 설정 해줘야함
        //일단 그냥 글자 화이트로 바꿔놓음
        view.addSpan(ForegroundColorSpan(Color.WHITE))
    }

}