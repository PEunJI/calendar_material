package com.example.calendar.Adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.calendar.OnedaySchedulesFragment

class OnedayDiffUtil(private val oldList: MutableList<ScheduleList>, private val currentList:  MutableList<ScheduleList>):
    DiffUtil.Callback(){
    override fun getOldListSize(): Int =oldList.size

    override fun getNewListSize(): Int =currentList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id==currentList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]==currentList[newItemPosition]
    }

}