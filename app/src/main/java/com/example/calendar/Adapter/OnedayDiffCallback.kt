package com.example.calendar.Adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.calendar.OnedaySchedulesFragment

class OnedayDiffCallback : DiffUtil.ItemCallback<ScheduleList>() {
    override fun areItemsTheSame(
        oldItem: ScheduleList,
        newItem: ScheduleList
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ScheduleList,
        newItem: ScheduleList
    ): Boolean {
        return oldItem==newItem
    }

}