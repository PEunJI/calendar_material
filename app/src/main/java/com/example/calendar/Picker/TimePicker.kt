package com.example.calendar.Picker

import android.app.TimePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.calendar.ScheduleEnrollFragment
import com.example.calendar.ScheduleEnrollFragment.Companion.end_liveHour
import com.example.calendar.ScheduleEnrollFragment.Companion.returnEndDate
import com.example.calendar.ScheduleEnrollFragment.Companion.returnEndHour
import com.example.calendar.ScheduleEnrollFragment.Companion.returnStartDay
import com.example.calendar.ScheduleEnrollFragment.Companion.returnStartHour
import com.example.calendar.ScheduleEnrollFragment.Companion.start_liveHour

class TimePicker(
    var context: Context,
    var hour: Int,
    var minute: Int
) {

    //시작시간 다이어로그
    val startTimeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        start_liveHour.value = "${hourOfDay}시 ${minute}분"
        returnStartHour[0] = hourOfDay.toLong()
        returnStartHour[1] = minute.toLong()
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
        end_liveHour.value = "${hourOfDay}시 ${minute}분"
        returnEndHour[0] = hourOfDay.toLong()
        returnEndHour[1] = minute.toLong()
        selectOnlyAfterStartTime(
            context, returnStartDay, returnStartHour, returnEndDate,
            returnEndHour, end_liveHour
        )
    }

    val timePickerDialog_end = TimePickerDialog(
        context,
        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
        timeSetListener_end,
        ScheduleEnrollFragment.returnStartHour[0].toInt() + 1,
        ScheduleEnrollFragment.returnStartHour[1].toInt(),
        true
    )

    companion object {
        fun selectOnlyAfterStartTime(
            context: Context,
            startD: ArrayList<Long>,
            startH: ArrayList<Long>,
            endD: ArrayList<Long>,
            endH: ArrayList<Long>,
            endLiveHourData: MutableLiveData<String>
        ) {

            var startdate_full =
                ("${startD[0]}${startD[1]}${startD[2]}").toInt()
            var endate_full =
                ("${endD[0]}${endD[1]}${endD[2]}").toInt()
            var startHour_full =
                ("${startH[0]}${startH[1]}").toInt()

            if (startdate_full < endate_full) {
                return
            } else {
                if (startHour_full > ("${endH[0]}${endH[1]}").toInt()) {
                    val temp = "${startH[0] + 1}시 ${startH[1]}분"
                    endLiveHourData.value = temp
                    Toast.makeText(context, "시작시간은 종료시간 전이여야 합니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}

