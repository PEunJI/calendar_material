package com.example.calendar.Adapter

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.calendar.Adapter.RecyclerViewAdapter.MyViewHolder
import com.example.calendar.BaseActivity.BaseActivity
import com.example.calendar.ReviseEnrollFragment
import com.example.calendar.databinding.ScheduleListBinding
import com.example.calendar.kakaoLogin.KakaoLogin
import com.example.calendar.kakaoLogin.MasterApplication
import kotlinx.coroutines.*
import okhttp3.Dispatcher

class RecyclerViewAdapter(val application: Application, val activity: Activity) :
    ListAdapter<Schedule, RecyclerView.ViewHolder>(OnedayDiffCallback()) {

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
                        runBlocking {
                            val job = CoroutineScope(Dispatchers.IO).async {
                                val scheduleId = data.id
                                (application as MasterApplication).service.deleteCalendar(
                                    "$scheduleId"
                                )
                            }

                            job.join()
                            Log.e("delete", "삭제완료")

                            val job2 = CoroutineScope(Dispatchers.IO).async {
                                Log.e("delete", "job2 coming")

                                KakaoLogin.myViewModel.getAlldayDots(activity)
                                KakaoLogin.myViewModel.updateOneDaySchedule(activity)
                            }.join()

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
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder) {
            val schedule = getItem(position) as Schedule
            holder.bind(schedule)

        }
    }

}

class LinearLayoutManagerWrapper : LinearLayoutManager {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, orientation: Int, reverseLayout: Boolean) : super(
        context,
        orientation,
        reverseLayout
    ) {
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
    }

    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }
}




