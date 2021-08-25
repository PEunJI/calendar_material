package com.example.calendar.Picker

import android.R
import android.app.TimePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData

class ReviseTimePicker (
    var mutableLiveData: MutableLiveData<String>,
    var context: Context,
    var mutableStartDate: MutableLiveData<Array<Long>>,
    var mutableEndDate: MutableLiveData<Array<Long>>,
    var mutableStartHour: MutableLiveData<Array<Long>>
) {
    val mutableEndHour = MutableLiveData<Array<Long>>()



    //시작시간 다이어로그
    val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        mutableLiveData.value = "${hourOfDay}시 ${minute}분"
        mutableStartHour.value = arrayOf(hourOfDay.toLong(), minute.toLong())
    }

    val timePickerDialog = TimePickerDialog(
        context,
        R.style.Theme_Holo_Light_Dialog_NoActionBar,
        timeSetListener,
        mutableStartHour.value!![0].toInt(),
        mutableStartHour.value!![1].toInt(),
        true
    )


    fun selectOnlyAfterStartTime() {

        var startdate_full =
            ("${mutableStartDate.value!![0]}${mutableStartDate.value!![1]}${mutableStartDate.value!![2]}").toInt()
        var endate_full =
            ("${mutableEndDate.value!![0]}${mutableEndDate.value!![1]}${mutableEndDate.value!![2]}").toInt()
        var startHour_full =
            ("${mutableStartHour.value!![0]}${mutableStartHour.value!![1]}").toInt()


        if (startdate_full < endate_full) {
            return
        } else {
            if (startHour_full > ("${mutableEndHour.value!![0]}${mutableEndHour.value!![1]}").toInt()) {
                mutableLiveData.value =
                    "${mutableStartHour.value!![0]+1}시 ${mutableStartHour.value!![1]}분"
                Toast.makeText(context, "시작시간은 종료시간 전이여야 합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    val timeSetListener_end = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        mutableLiveData.value = "${hourOfDay}시 ${minute}분"
        mutableEndHour.value = arrayOf(hourOfDay.toLong(), minute.toLong())
        selectOnlyAfterStartTime()
    }

    val timePickerDialog_end = TimePickerDialog(
        context,
        R.style.Theme_Holo_Light_Dialog_NoActionBar,
        timeSetListener_end,
        mutableStartHour.value!![0].toInt() + 1,
        mutableStartHour.value!![1].toInt(),
        true
    )
}