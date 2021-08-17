package com.example.calendar

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.example.calendar.Picker.DatePicker
import com.example.calendar.Picker.TimePicker
import com.example.calendar.databinding.ActivityMainBinding
import com.example.calendar.databinding.FragmentScheduleEnrollBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.*


class ScheduleEnrollFragment : BottomSheetDialogFragment() {

    //현재 날짜
    val c: Calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))

    var mYear: Int = 0
    var mMonth: Int = 0
    var mDay: Int = 0
    val mHour: Int = c.get(Calendar.HOUR_OF_DAY)
    val mMinute: Int = c.get(Calendar.MINUTE)

    val start_liveDate = MutableLiveData<String>()
    val start_liveHour = MutableLiveData<String>()
    val end_liveDate = MutableLiveData<String>()
    val end_liveHour = MutableLiveData<String>()

    lateinit var get_context: Activity
    lateinit var binding: FragmentScheduleEnrollBinding
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            get_context = context as Activity
        }
        mYear = arguments?.getString("year")!!.toInt()
        mMonth = arguments?.getString("month")!!.toInt()
        mDay = arguments?.getString("day")!!.toInt()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_schedule_enroll, container, false)
        binding.apply {
            lifecycleOwner = this@ScheduleEnrollFragment
            main = this@ScheduleEnrollFragment
        }

        //첫세팅 (누른날짜~누시간+1시간)

        start_liveDate.value = "${mYear}년 ${mMonth}월 ${mDay}일 "
        start_liveHour.value = "${mHour}시 ${mMinute}분"
        end_liveDate.value = "${mYear}년 ${mMonth}월 ${mDay}일 "
        end_liveHour.value = "${mHour + 1}시 ${mMinute}분"
        Log.d("dateTest2","${mMonth}월")
        binding.btnCancle.setOnClickListener {
            dismiss()
        }

        val startdate_picker = DatePicker(start_liveDate, get_context, mYear, mMonth, mDay)
        val starthour_picker = TimePicker(start_liveHour, get_context, mHour, mMinute)
        val enddate_picker = DatePicker(end_liveDate, get_context, mYear, mMonth, mDay)

        var returnStartDay: Array<Long> = arrayOf(0, 0, 0)

        binding.startDatePicker.setOnClickListener {
            startdate_picker.datePickerDialog.show()
            returnStartDay = startdate_picker.returnStartDate()
        }
        var returnStartHour: Array<Long> = arrayOf(0, 0, 0)
        binding.startTimePicker.setOnClickListener {

            starthour_picker.timePickerDialog.apply {
                this.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            }.show()
            returnStartHour = starthour_picker.returnStartHour()
        }

        var returnEndDate: Array<Long> = arrayOf(0, 0, 0)

        binding.endDatePicker.setOnClickListener {
            //종료 날짜는 시작날짜 이후로만 선택되게

            //날짜 String->dateFormat->TimeMillis()
            var date =
                "${returnStartDay[0]}-${returnStartDay[1]}-${returnStartDay[2]} ${returnStartHour[0]}:${returnStartHour[1]}"
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val dateFormat = simpleDateFormat.parse(date)
            enddate_picker.datePickerDialog.apply {
                datePicker.minDate = (dateFormat.time) - 1000
            }.show()
            returnEndDate = enddate_picker.returnStartDate()
            Log.d("dateTestt",date)
        }
        var returnEndHour: Array<Long> = arrayOf(0L)

        var startDateFull = "${returnStartDay[0]}${returnStartDay[1]}${returnStartDay[2]}"
        var startHourFull = "${returnStartHour[0]}${returnStartHour[1]}"
        var endDateFull = "${returnEndDate[0]}${returnEndDate[1]}${returnEndDate[2]}"

        val endhour_picker = TimePicker(
            end_liveHour,
            get_context,
            mHour + 1,
            mMinute,
            startDateFull.toInt(),
            endDateFull.toInt(),
            startHourFull.toInt()
        )


        //종료 시간은 시작 시간 이후로만 선택되게 하고 싶지만 커스텀해야하는 것 같다. 구찮다. 그냥 토스트를 띄우고 스타트타임+1시간으로 고정시키자
        binding.endTimePicker.setOnClickListener {

            endhour_picker.timePickerDialog.apply {
                endhour_picker.timeSetListener.apply {
                    endhour_picker.selectOnlyAfterStartTime()
                }
            }.show()
            returnEndHour = endhour_picker.returnStartHour()
        }

        return binding.root
    }
}

