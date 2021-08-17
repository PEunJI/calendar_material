package com.example.calendar.Picker

import android.app.TimePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.calendar.R

class TimePicker(mutableLiveData: MutableLiveData<String>, var context: Context, var mHour : Int, var mMinute: Int, startDate : Int=0, endDate : Int=0, startHour : Int =0){

    var startDate : Int =0
    var endDate : Int =0
    var startHour : Int =0

    init {
        this.startDate=startDate
        this.endDate=endDate
        this.startHour = startHour
    }
    var returnHour =0L
    var returnMin=0L

    var endHour="null"

    val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        mutableLiveData.value  = "${hourOfDay}시 ${minute}분"
        returnHour=hourOfDay.toLong()
        returnMin=minute.toLong()
        endHour = hourOfDay.toString()+minute.toString()
    }

    fun returnStartHour() : Array<Long> {
        val returns = arrayOf<Long>(returnHour,returnMin)

        if(returnHour==0L || returnMin==0L){
            returns[0]=mHour.toLong()
            returns[1]=mMinute.toLong()
            return returns
        }

        return returns
    }

    fun selectOnlyAfterStartTime(){
        if(this.startDate<this.endDate){
            return
        }
        else {
            if(startHour>endHour.toInt()){
                Toast.makeText(context,"종료 시간은 시작 시간 전이여야 합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }



    val timePickerDialog = TimePickerDialog(
        context,
        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
        timeSetListener,
        mHour,
        mMinute,
        true
    )
}