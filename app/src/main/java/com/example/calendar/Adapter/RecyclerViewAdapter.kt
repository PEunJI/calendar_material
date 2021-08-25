package com.example.calendar.Adapter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.calendar.*
import com.example.calendar.BaseActivity.BaseActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class RecyclerViewAdapter(val itemList: MutableList<ScheduleList>, val inflater: LayoutInflater) :
//상속받는 함수의 타입은 이너클래스에서 만든 viewholder을 넣어준다.
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHodler>() {

    //RecyclerViewAdapter의 매게변수 itemlist를 쓰기 위해서 inner클래스 임을 명시해준다.
    //그렇지 않으면, outer 클래스의 매개변수를 사용할 수 없음.
    //2. findViewById로 textview를 찾아서 viewholder에 저장한다.
    inner class ViewHodler(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView
        val start: TextView
        val end: TextView
        val memo: TextView
        val revise: Button
        val delete: Button

        init {
            title = itemView.findViewById(R.id.title)
            start = itemView.findViewById(R.id.start)
            end = itemView.findViewById(R.id.end)
            memo = itemView.findViewById(R.id.memo)
            revise = itemView.findViewById(R.id.revise)
            delete = itemView.findViewById(R.id.delete)

        }

    }

    //1. item_view layout을 객체화시켜준다.
    // ViewHolder class의 매개변수로 넣어준다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHodler {
        val view = inflater.inflate(R.layout.schedule_list, parent, false)
        return ViewHodler(view)
    }

    //3. list[position]의 내용물을 저장된 textview(holder.view)에 넣어준다.
    override fun onBindViewHolder(holder: ViewHodler, position: Int) {
        holder.title.setText(itemList.get(position).title)
        holder.start.setText(itemList.get(position).start)
        holder.end.setText(itemList.get(position).end)
        holder.memo.setText(itemList.get(position).memo)

        val endfull = itemList.get(position).end
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

        val startfull = itemList.get(position).start
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

        holder.revise.setOnClickListener {
            val bottomSheet = ReviseEnrollFragment()
            bottomSheet.apply {
                arguments = Bundle().apply {
                    try {
                        putString("memo", itemList[position].memo)
                    } catch (e: Exception) {
                    }
                    putString("title", itemList[position].title)
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
                    putInt("id", itemList[position].id!!)

                }
            }.show(BaseActivity.fragmentManager!!, bottomSheet.tag)
        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}