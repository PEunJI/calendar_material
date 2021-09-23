package com.example.calendar.Adapter

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import io.reactivex.internal.util.EmptyComponent
import java.lang.Exception

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
        return oldItem == newItem
    }

}