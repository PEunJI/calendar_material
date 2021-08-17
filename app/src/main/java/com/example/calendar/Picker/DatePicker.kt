package com.example.calendar.Picker

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData

class DatePicker(mutableLiveData: MutableLiveData<String>, context: Context, var mYear : Int, var mMonth: Int, var mDay : Int){


    var returnYear =0L
    var returnMonth=0L
    var returnDay=0L

    val dateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            mutableLiveData.value = "${year}년 ${month}월 ${dayOfMonth}일"
            returnYear=year.toLong()
            returnMonth=month.toLong()
            returnDay=dayOfMonth.toLong()
        Log.d("monthTest",""+returnMonth)
        }


    fun returnStartDate() : Array<Long> {
        val returns = arrayOf<Long>(returnYear,returnMonth,returnDay)

        if(returnYear==0L || returnMonth==0L || returnDay==0L){
            returns[0]=mYear.toLong()
            returns[1]=mMonth.toLong()
            returns[2]=mDay.toLong()
            return returns
        }

        return returns
    }

    val datePickerDialog = DatePickerDialog(
        context,
        dateSetListener,
        mYear, mMonth, mDay
    )

}