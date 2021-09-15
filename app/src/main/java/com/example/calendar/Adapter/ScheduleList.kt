package com.example.calendar.Adapter

data class ScheduleList(
    var title: String? = null,
    var start: String? = null,
    var end: String? = null,
    var memo: String? = null,
    var id: Int? = null
) {

    companion object {

        val MutablescheduleList = mutableListOf<ScheduleList>()



    }


}