package com.example.calendar.Adapter

import androidx.recyclerview.widget.DiffUtil

class OnedayDiffCallback : DiffUtil.ItemCallback<Schedule>() {
    override fun areItemsTheSame(
        oldItem: Schedule,
        newItem: Schedule
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Schedule,
        newItem: Schedule
    ): Boolean {
        return oldItem==newItem
    }

}