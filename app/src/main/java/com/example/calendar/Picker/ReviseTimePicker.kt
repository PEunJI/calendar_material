package com.example.calendar.Picker

import android.R
import android.app.TimePickerDialog
import android.content.Context
import com.example.calendar.Picker.TimePicker.Companion.selectOnlyAfterStartTime
import com.example.calendar.ReviseEnrollFragment
import com.example.calendar.ReviseEnrollFragment.Companion.end_liveHour_revise
import com.example.calendar.ReviseEnrollFragment.Companion.returnEndDate_revise
import com.example.calendar.ReviseEnrollFragment.Companion.returnEndHour_revise
import com.example.calendar.ReviseEnrollFragment.Companion.returnStartDay_revise
import com.example.calendar.ReviseEnrollFragment.Companion.returnStartHour_revise

class ReviseTimePicker(
    var context: Context,
) {


    //시작시간 다이어로그
    val startTimeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        ReviseEnrollFragment.start_liveHour_revise.value = "${hourOfDay}시 ${minute}분"
        returnStartHour_revise[0] = hourOfDay.toLong()
        returnStartHour_revise[1] = minute.toLong()
    }

    val startTimePickerDialog = TimePickerDialog(
        context,
        R.style.Theme_Holo_Light_Dialog_NoActionBar,
        startTimeSetListener,
        returnStartHour_revise[0].toInt(),
        returnStartHour_revise[1].toInt(),
        true
    )


    val timeSetListener_end = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        end_liveHour_revise.value = "${hourOfDay}시 ${minute}분"
        returnEndHour_revise[0] = hourOfDay.toLong()
        returnEndHour_revise[1] = minute.toLong()
        selectOnlyAfterStartTime(
            context, returnStartDay_revise, returnStartHour_revise, returnEndDate_revise,
            returnEndHour_revise, end_liveHour_revise
        )
    }

    val timePickerDialog_end = TimePickerDialog(
        context,
        R.style.Theme_Holo_Light_Dialog_NoActionBar,
        timeSetListener_end,
        returnStartHour_revise[0].toInt(),
        returnStartHour_revise[1].toInt(),
        true
    )



}