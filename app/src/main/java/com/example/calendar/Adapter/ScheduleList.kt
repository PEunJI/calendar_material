package com.example.calendar.Adapter

class ScheduleList( var title : String? = null,
                    var start : String? = null,
                    var end : String? = null,
                    var memo : String? = null) {


    companion object{

        val scheduleList = ArrayList<ScheduleList>()

    }



}