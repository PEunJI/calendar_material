package com.example.calendar.Picker

import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.calendar.R
import com.example.calendar.ScheduleEnrollFragment

class TimePicker(
    var context: Context,
    var hour: Int,
    var minute: Int
) {

    //시작시간 다이어로그
    val startTimeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        ScheduleEnrollFragment.start_liveHour.value = "${hourOfDay}시 ${minute}분"
        ScheduleEnrollFragment.returnStartHour[0] = hourOfDay.toLong()
        ScheduleEnrollFragment.returnStartHour[1] = minute.toLong()
    }

    val startTimePickerDialog = TimePickerDialog(
        context,
        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
        startTimeSetListener,
        hour,
        minute,
        true
    )


    val timeSetListener_end = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        ScheduleEnrollFragment.end_liveHour.value = "${hourOfDay}시 ${minute}분"
        ScheduleEnrollFragment.returnEndHour[0] = hourOfDay.toLong()
        ScheduleEnrollFragment.returnEndHour[1] = minute.toLong()
        selectOnlyAfterStartTime()
    }

    val timePickerDialog_end = TimePickerDialog(
        context,
        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
        timeSetListener_end,
        ScheduleEnrollFragment.returnStartHour[0].toInt() + 1,
        ScheduleEnrollFragment.returnStartHour[1].toInt(),
        true
    )

    fun selectOnlyAfterStartTime() {

        var startdate_full =
            ("${ScheduleEnrollFragment.returnStartDay[0]}${ScheduleEnrollFragment.returnStartDay[1]}${ScheduleEnrollFragment.returnStartDay[2]}").toInt()
        var endate_full =
            ("${ScheduleEnrollFragment.returnEndDate[0]}${ScheduleEnrollFragment.returnEndDate[1]}${ScheduleEnrollFragment.returnEndDate[2]}").toInt()
        var startHour_full =
            ("${ScheduleEnrollFragment.returnStartHour[0]}${ScheduleEnrollFragment.returnStartHour[1]}").toInt()

        if (startdate_full < endate_full) {
            return
        } else {
            if (startHour_full > ("${ScheduleEnrollFragment.returnEndHour[0]}${ScheduleEnrollFragment.returnEndHour[1]}").toInt()) {
                 val temp =      "${ScheduleEnrollFragment.returnStartHour[0] + 1}시 ${ScheduleEnrollFragment.returnStartHour[1]}분"
                ScheduleEnrollFragment.end_liveHour.value = temp
                Toast.makeText(context, "시작시간은 종료시간 전이여야 합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}