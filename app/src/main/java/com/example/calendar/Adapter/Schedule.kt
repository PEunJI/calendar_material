package com.example.calendar.Adapter

data class Schedule(
    var title: String? = null,
    var start: String? = null,
    var end: String? = null,
    var memo: String? = null,
    var id: Int? = null
) {

    companion object {

        val MutablescheduleList = mutableListOf<Schedule>()

    }


}