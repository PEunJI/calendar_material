package com.example.calendar.Picker

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData

class DatePicker(
    mutableLiveData: MutableLiveData<String>,
   var returnStartDay: MutableLiveData<Array<Long>>,
    context: Context,
    mYear: Int,
    mMonth: Int,
    mDay: Int
) {
    var mutableLiveData_end: MutableLiveData<String>? = null

    constructor(
        mutableLiveData: MutableLiveData<String>,
        returnStartDay: MutableLiveData<Array<Long>>,
        context: Context,
        mYear: Int,
        mMonth: Int,
        mDay: Int, mutableLiveData_end: MutableLiveData<String>?
    ) : this(mutableLiveData, returnStartDay, context, mYear, mMonth, mDay) {
        this.mutableLiveData_end = mutableLiveData_end
    }

    var returnYear = 0L
    var returnMonth = 0L
    var returnDay = 0L

    val dateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            mutableLiveData.value = "${year}년 ${month + 1}월 ${dayOfMonth}일 "
            returnYear = year.toLong()
            returnMonth = month.toLong() + 1
            returnDay = dayOfMonth.toLong()
            Log.d("monthTest", "" + month + "day" + dayOfMonth)
            Log.d("monthTest", "return" + returnMonth + returnDay)
            returnStartDate()
            mutableLiveData_end?.value= "${year}년 ${month + 1}월 ${dayOfMonth}일 "
        }


    fun returnStartDate() {
        returnStartDay.value = arrayOf<Long>(returnYear, returnMonth, returnDay)
    }

    val datePickerDialog = DatePickerDialog(
        context,
        dateSetListener,
        mYear, mMonth - 1, mDay

    )

}