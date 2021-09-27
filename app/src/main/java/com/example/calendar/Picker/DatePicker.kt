package com.example.calendar.Picker

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.calendar.ScheduleEnrollFragment
import com.example.calendar.ScheduleEnrollFragment.Companion.end_liveDate
import com.example.calendar.ScheduleEnrollFragment.Companion.returnEndDate
import com.example.calendar.ScheduleEnrollFragment.Companion.returnStartDay
import com.example.calendar.ScheduleEnrollFragment.Companion.start_liveDate

class DatePicker(

    val context: Context,
    val mYear: Int,
    val mMonth: Int,
    val mDay: Int
) {


    val startDateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            start_liveDate.value = "${year}년 ${month + 1}월 ${dayOfMonth}일 "
            returnStartDay[0] = year.toLong()
            returnStartDay[1] = month.toLong() + 1
            returnStartDay[2] = dayOfMonth.toLong()
            returnEndDate[0] = year.toLong()
            returnEndDate[1] = month.toLong() + 1
            returnEndDate[2] = dayOfMonth.toLong()
            end_liveDate.value = "${year}년 ${month + 1}월 ${dayOfMonth}일 "
        }

    val EndDateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            returnEndDate[0] = year.toLong()
            returnEndDate[1] = month.toLong() + 1
            returnEndDate[2] = dayOfMonth.toLong()
            end_liveDate.value = "${year}년 ${month + 1}월 ${dayOfMonth}일 "
        }


    val startDatePickerDialog = DatePickerDialog(
        context,
        startDateSetListener,
        mYear, mMonth - 1, mDay
    )

    val endDatePickerDialog = DatePickerDialog(
        context,
        EndDateSetListener,
        returnStartDay[0].toInt(), returnStartDay[1].toInt()-1, returnStartDay[2].toInt()
    )

}