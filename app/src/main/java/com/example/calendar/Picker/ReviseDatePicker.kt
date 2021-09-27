package com.example.calendar.Picker

import android.app.DatePickerDialog
import android.content.Context
import com.example.calendar.ReviseEnrollFragment

class ReviseDatePicker(
    context: Context,
    mYear: Int,
    mMonth: Int,
    mDay: Int
) {
    val startDateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            ReviseEnrollFragment.start_liveDate_revise.value =
                "${year}년 ${month + 1}월 ${dayOfMonth}일 "
            ReviseEnrollFragment.returnStartDay_revise[0] = year.toLong()
            ReviseEnrollFragment.returnStartDay_revise[1] = month.toLong() + 1
            ReviseEnrollFragment.returnStartDay_revise[2] = dayOfMonth.toLong()
            ReviseEnrollFragment.returnEndDate_revise[0] = year.toLong()
            ReviseEnrollFragment.returnEndDate_revise[1] = month.toLong() + 1
            ReviseEnrollFragment.returnEndDate_revise[2] = dayOfMonth.toLong()
        }


    val startDatePickerDialog = DatePickerDialog(
        context,
        startDateSetListener,
        mYear, mMonth - 1, mDay

    )

    val EndDateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            ReviseEnrollFragment.returnEndDate_revise[0] = year.toLong()
            ReviseEnrollFragment.returnEndDate_revise[1] = month.toLong() + 1
            ReviseEnrollFragment.returnEndDate_revise[2] = dayOfMonth.toLong()
            ReviseEnrollFragment.end_liveDate_revise.value =
                "${year}년 ${month + 1}월 ${dayOfMonth}일 "
        }


    val endDatePickerDialog = DatePickerDialog(
        context,
        EndDateSetListener,
        ReviseEnrollFragment.returnEndDate_revise[0].toInt(),
        ReviseEnrollFragment.returnEndDate_revise[1].toInt() - 1,
        ReviseEnrollFragment.returnEndDate_revise[2].toInt()
    )


}