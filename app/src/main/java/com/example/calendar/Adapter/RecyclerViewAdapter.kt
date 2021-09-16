package com.example.calendar.Adapter

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.calendar.BaseActivity.BaseActivity
import com.example.calendar.ReviseEnrollFragment
import com.example.calendar.databinding.ScheduleListBinding
import com.example.calendar.kakaoLogin.MasterApplication
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RecyclerViewAdapter(val application: Application) :ListAdapter<Schedule,RecyclerView.ViewHolder>(OnedayDiffCallback()) {

    inner class MyViewHolder(private val binding: ScheduleListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Schedule) {
            with(binding) {
                title.text = data.title
                memo.text = data.memo
                start.text = data.start
                end.text = data.end


                val endfull = data.end
                val dateHourSplit_e = endfull!!.split(" ")
                val datefull_e = dateHourSplit_e[0]
                val hourfull_e = dateHourSplit_e[1]
                val datesplit_e = datefull_e.split("-")
                val hoursplit_e = hourfull_e.split(":")
                val endY = datesplit_e[0]
                val endM = datesplit_e[1]
                val endD = datesplit_e[2]
                val endH = hoursplit_e[0]
                val endm = hoursplit_e[1]

                val startfull = data.start
                val dateHourSplit = startfull!!.split(" ")
                val datefull = dateHourSplit[0]
                val hourfull = dateHourSplit[1]
                val datesplit = datefull.split("-")
                val hoursplit = hourfull.split(":")
                val startY = datesplit[0]
                val startM = datesplit[1]
                val startD = datesplit[2]
                val startH = hoursplit[0]
                val startm = hoursplit[1]


                revise.setOnClickListener {
                    val bottomSheet = ReviseEnrollFragment()
                    bottomSheet.apply {
                        arguments = Bundle().apply {
                            try {
                                putString("memo", data.memo)
                            } catch (e: Exception) {
                            }
                            putString("title", data.title)
                            putString("startY", startY)
                            putString("startM", startM)
                            putString("startD", startD)
                            putString("startH", startH)
                            putString("startm", startm)
                            putString("endY", endY)
                            putString("endM", endM)
                            putString("endD", endD)
                            putString("endH", endH)
                            putString("endm", endm)
                            putInt("id", data.id!!)

                        }
                    }.show(BaseActivity.fragmentManager!!, bottomSheet.tag)
                }
                delete.setOnClickListener {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(delete.context)
                    val ad = builder.create()
                    ad.show()
                    builder.setTitle("삭제")
                    builder.setMessage("삭제하시겠습니까?")
                    builder.setPositiveButton("예") { dialog, id ->
                        //삭제
                        val scheduleId = data.id
                        GlobalScope.launch {
                            (application as MasterApplication).service.deleteCalendar(
                                "$scheduleId"
                            )
                        }
                        ad.dismiss()
                    }
                    builder.setNegativeButton("아니오") { dialog, id ->
                        ad.dismiss()
                    }
                    builder.create().show()
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = MyViewHolder(
            ScheduleListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        return  viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is MyViewHolder){
            val schedule = getItem(position) as Schedule
            holder.bind(schedule)

        }
    }

}





