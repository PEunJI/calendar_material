package com.example.calendar.Adapter

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.calendar.*
import com.example.calendar.BaseActivity.BaseActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*
import android.widget.Toast

import android.content.DialogInterface
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.calendar.Retrofit.RetrofitService
import com.example.calendar.databinding.ScheduleListBinding
import com.example.calendar.kakaoLogin.KakaoLogin


//class RecyclerViewAdapter(val itemList: MutableList<ScheduleList>, val inflater: LayoutInflater) :
////상속받는 함수의 타입은 이너클래스에서 만든 viewholder을 넣어준다.
//    RecyclerView.Adapter<RecyclerViewAdapter.ViewHodler>() {
//
//    //RecyclerViewAdapter의 매게변수 itemlist를 쓰기 위해서 inner클래스 임을 명시해준다.
//    //그렇지 않으면, outer 클래스의 매개변수를 사용할 수 없음.
//    //2. findViewById로 textview를 찾아서 viewholder에 저장한다.
////    inner class ViewHodler(itemView: View) : RecyclerView.ViewHolder(itemView) {
////        val title: TextView
////        val start: TextView
////        val end: TextView
////        val memo: TextView
////        val revise: Button
////        val delete: Button
////
////        init {
////            title = itemView.findViewById(R.id.title)
////            start = itemView.findViewById(R.id.start)
////            end = itemView.findViewById(R.id.end)
////            memo = itemView.findViewById(R.id.memo)
////            revise = itemView.findViewById(R.id.revise)
////            delete = itemView.findViewById(R.id.delete)
////
////        }
//
//    inner class ViewHolder(private val binding: ScheduleListBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        fun bind(data: ScheduleList) {
//            with(binding) {
//                title.text =data.title
//                memo.text =data.memo
//                start.text =data.start
//                end.text =data.end
//
//
//                val endfull = data.end
//                val dateHourSplit_e = endfull!!.split(" ")
//                val datefull_e = dateHourSplit_e[0]
//                val hourfull_e = dateHourSplit_e[1]
//                val datesplit_e = datefull_e.split("-")
//                val hoursplit_e = hourfull_e.split(":")
//                val endY = datesplit_e[0]
//                val endM = datesplit_e[1]
//                val endD = datesplit_e[2]
//                val endH = hoursplit_e[0]
//                val endm = hoursplit_e[1]
//
//                val startfull = data.start
//                val dateHourSplit = startfull!!.split(" ")
//                val datefull = dateHourSplit[0]
//                val hourfull = dateHourSplit[1]
//                val datesplit = datefull.split("-")
//                val hoursplit = hourfull.split(":")
//                val startY = datesplit[0]
//                val startM = datesplit[1]
//                val startD = datesplit[2]
//                val startH = hoursplit[0]
//                val startm = hoursplit[1]
//
//
//                revise.setOnClickListener {
//                    val bottomSheet = ReviseEnrollFragment()
//                    bottomSheet.apply {
//                        arguments = Bundle().apply {
//                            try {
//                                putString("memo", itemList[position].memo)
//                            } catch (e: Exception) {
//                            }
//                            putString("title", itemList[position].title)
//                            putString("startY", startY)
//                            putString("startM", startM)
//                            putString("startD", startD)
//                            putString("startH", startH)
//                            putString("startm", startm)
//                            putString("endY", endY)
//                            putString("endM", endM)
//                            putString("endD", endD)
//                            putString("endH", endH)
//                            putString("endm", endm)
//                            putInt("id", data.id!!)
//
//                        }
//                    }.show(BaseActivity.fragmentManager!!, bottomSheet.tag)
//                }
//                delete.setOnClickListener {
//                    val builder: AlertDialog.Builder = AlertDialog.Builder(delete.context)
//                    val ad = builder.create()
//                    ad.show()
//                    builder.setTitle("삭제")
//                    builder.setMessage("삭제하시겠습니까?")
//                    builder.setPositiveButton("예") { dialog, id ->
//                        //삭제
//                        val scheduleId = data.id
//                        GlobalScope.launch {
//                            RetrofitService.service.deleteCalendar("$scheduleId","${KakaoLogin.user_id}")
//                        }
//                        ad.dismiss()
//                    }
//                    builder.setNegativeButton("아니오") { dialog, id ->
//                        ad.dismiss()
//                    }
//                    builder.create().show()
//                }
//
//            }
//        }
//    }
//
//
//    //1. item_view layout을 객체화시켜준다.
//    // ViewHolder class의 매개변수로 넣어준다.
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHodler {
//        val view = inflater.inflate(R.layout.schedule_list, parent, false)
//        return ViewHodler(view)
//    }
//
//    //3. list[position]의 내용물을 저장된 textview(holder.view)에 넣어준다.
//    override fun onBindViewHolder(holder: ViewHodler, position: Int) {
//
//        updateAdapter()
//
//        holder.title.setText(itemList.get(position).title)
//        holder.start.setText(itemList.get(position).start)
//        holder.end.setText(itemList.get(position).end)
//        holder.memo.setText(itemList.get(position).memo)
//
//        val endfull = itemList.get(position).end
//        val dateHourSplit_e = endfull!!.split(" ")
//        val datefull_e = dateHourSplit_e[0]
//        val hourfull_e = dateHourSplit_e[1]
//        val datesplit_e = datefull_e.split("-")
//        val hoursplit_e = hourfull_e.split(":")
//        val endY = datesplit_e[0]
//        val endM = datesplit_e[1]
//        val endD = datesplit_e[2]
//        val endH = hoursplit_e[0]
//        val endm = hoursplit_e[1]
//
//        val startfull = itemList.get(position).start
//        val dateHourSplit = startfull!!.split(" ")
//        val datefull = dateHourSplit[0]
//        val hourfull = dateHourSplit[1]
//        val datesplit = datefull.split("-")
//        val hoursplit = hourfull.split(":")
//        val startY = datesplit[0]
//        val startM = datesplit[1]
//        val startD = datesplit[2]
//        val startH = hoursplit[0]
//        val startm = hoursplit[1]
//
//        holder.revise.setOnClickListener {
//            val bottomSheet = ReviseEnrollFragment()
//            bottomSheet.apply {
//                arguments = Bundle().apply {
//                    try {
//                        putString("memo", itemList[position].memo)
//                    } catch (e: Exception) {
//                    }
//                    putString("title", itemList[position].title)
//                    putString("startY", startY)
//                    putString("startM", startM)
//                    putString("startD", startD)
//                    putString("startH", startH)
//                    putString("startm", startm)
//                    putString("endY", endY)
//                    putString("endM", endM)
//                    putString("endD", endD)
//                    putString("endH", endH)
//                    putString("endm", endm)
//                    putInt("id", itemList[position].id!!)
//
//                }
//            }.show(BaseActivity.fragmentManager!!, bottomSheet.tag)
//        }
//        //삭제 버튼
//        holder.delete.setOnClickListener {
//            val builder: AlertDialog.Builder = AlertDialog.Builder(holder.delete.context)
//            val ad = builder.create()
//            ad.show()
//            builder.setTitle("삭제")
//            builder.setMessage("삭제하시겠습니까?")
//            builder.setPositiveButton("예") { dialog, id ->
//            //삭제
//                val scheduleId = itemList[position].id
//                GlobalScope.launch {
//                    RetrofitService.service.deleteCalendar("$scheduleId","${KakaoLogin.user_id}")
//                }
//                ad.dismiss()
//            }
//            builder.setNegativeButton("아니오") { dialog, id ->
//                    ad.dismiss()
//            }
//            builder.create().show()
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return itemList.size
//    }
//
//    fun updateAdapter(){
//
//        var oldList = OnedaySchedulesFragment.oneDayMutable
//
//        ScheduleList.MutablescheduleList.clear()
//        OnedaySchedulesFragment.oneDayMutable.clear()
//        /**스케줄 다시 가져오기**/
//        ScheduleList.getScheules()
//        OnedaySchedulesFragment.oneDayMutableList()
//        val newList = OnedaySchedulesFragment.oneDayMutable
//
//        for(i in 0 until oldList.size ){
//        Log.d("updateAdapter",oldList[i].memo.toString())}
//        for(i in 0 until newList.size ){
//            Log.d("updateAdapter",newList[i].memo.toString())}
//        val oneDiffUtilCallback = OnedayDiffUtil(oldList, newList)
//        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(oneDiffUtilCallback)
//       this.run{
//        diffResult.dispatchUpdatesTo(this)}
//    }
//
//
//}

class RecyclerViewAdapter :ListAdapter<ScheduleList,RecyclerView.ViewHolder>(OnedayDiffCallback()) {
    inner class MyViewHolder(private val binding: ScheduleListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ScheduleList) {
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
                            RetrofitService.service.deleteCalendar(
                                "$scheduleId",
                                "${KakaoLogin.user_id}"
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
            val schedule = getItem(position) as ScheduleList
            holder.bind(schedule)
        }
    }

}





