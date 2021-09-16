package com.example.calendar

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.example.calendar.Picker.DatePicker
import com.example.calendar.Picker.TimePicker
import com.example.calendar.databinding.FragmentScheduleEnrollBinding
import com.example.calendar.kakaoLogin.KakaoLogin.Companion.myViewModel
import com.example.calendar.kakaoLogin.MasterApplication
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
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


    //다이어로그 펼쳐진 상태로 보이게하기
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = BottomSheetDialog(
            requireContext(),
            theme
        )
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (bottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior = BottomSheetBehavior.from<View>(bottomSheet!!)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return bottomSheetDialog

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
        Log.d("dateTest2", "${mMonth}월")


        //cancle button
        binding.btnCancle.setOnClickListener {
            dismiss()
        }


        //return 변수 초기화
        var returnStartDay = MutableLiveData<Array<Long>>()
        var returnEndDate = MutableLiveData<Array<Long>>()
        var returnStartHour = MutableLiveData<Array<Long>>()
        var returnEndHour = MutableLiveData<Array<Long>>()

        returnStartDay.value = arrayOf(mYear.toLong(), mMonth.toLong(), mDay.toLong())
        returnEndDate.value = arrayOf(mYear.toLong(), mMonth.toLong(), mDay.toLong())
        returnStartHour.value = arrayOf(mHour.toLong(), mMinute.toLong())
        returnEndHour.value = arrayOf(mHour.toLong() + 1, mMinute.toLong())


        val startdate_picker =
            DatePicker(
                start_liveDate,
                returnStartDay,
                get_context,
                mYear,
                mMonth,
                mDay,
                end_liveDate
            )
        val enddate_picker =
            DatePicker(
                end_liveDate,
                returnEndDate,
                get_context,
                mYear,
                mMonth,
                mDay
            )
        val starthour_picker =
            TimePicker(start_liveHour, get_context, returnStartDay, returnEndDate, returnStartHour)

        //시작 날짜 눌렀을 때 datepicker띄우기
        binding.startDatePicker.setOnClickListener {
            startdate_picker.datePickerDialog.show()

        }

        //시작 시간 눌렀을 때 timepicker띄우기
        binding.startTimePicker.setOnClickListener {
            starthour_picker.timePickerDialog.apply {
                this.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            }.show()

        }

        //종료 날짜 눌렀을 때 datepicker띄우기
        binding.endDatePicker.setOnClickListener {
            //종료 날짜는 시작날짜 이후로만 선택되게
            //날짜 String->dateFormat->TimeMillis()
            var date =
                "${returnStartDay.value!![0]}-${returnStartDay.value!![1]}-${returnStartDay.value!![2]} ${returnStartHour.value!![0]}:${returnStartHour.value!![1]}"
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
            Log.d("dateLog", "" + returnStartDay.value!![2])
            val dateFormat = simpleDateFormat.parse(date)

            enddate_picker.datePickerDialog.apply {
                datePicker.minDate = (dateFormat.time)
            }.show()
            Log.d("dateTest", date)

        }

        val endhour_picker = TimePicker(
            end_liveHour,
            get_context,
            returnStartDay,
            returnEndDate,
            returnStartHour
        )


        //종료 시간은 시작 시간 이후로만 선택되게 하고 싶지만 커스텀해야하는 것 같다. 구찮다. 그냥 토스트를 띄우고 스타트타임+1시간으로 고정시키자
        binding.endTimePicker.setOnClickListener {

            endhour_picker.timePickerDialog_end.show()

        }

        //취소버튼
        binding.btnCancle.setOnClickListener {
            dismiss()
        }

        //저장버튼
        binding.btnEnroll.setOnClickListener {
            var input = HashMap<String, String>()

            //시간체크
            if (
                (start_liveDate.value.toString() + start_liveHour.value.toString()).replace(
                    ("[^\\d.]").toRegex(),
                    ""
                ).toLong() >
                (end_liveDate.value.toString() + end_liveHour.value.toString()).replace(
                    ("[^\\d.]").toRegex(),
                    ""
                ).toLong()
            ) {
                Toast.makeText(get_context, "시작시간은 종료시간 전이여야 합니다.", Toast.LENGTH_SHORT).show()
            } else {
                //제목이 null일때
                if (binding.txtTitle.text.toString() == "") {
                    Toast.makeText(get_context, "제목은 필수입니다.", Toast.LENGTH_SHORT).show()
                } else {
                    //등록
                    enrollCoroutine = runBlocking {

                        withContext(Dispatchers.IO) {
                            input["dateStart"] =
                                "${returnStartDay.value!![0]}-${returnStartDay.value!![1]}-${returnStartDay.value!![2]} ${returnStartHour.value!![0]}:${returnStartHour.value!![1]}"
                            input["dateEnd"] =
                                "${returnEndDate.value!![0]}-${returnEndDate.value!![1]}-${returnEndDate.value!![2]} ${returnEndHour.value!![0]}:${returnEndHour.value!![1]}"
                            input["content"] =
                                binding.txtTitle.text.toString() + "@^" + binding.txtMemo.text?.toString()
                            (requireActivity().application as MasterApplication).service.postCalendar(
                                input
                            )
                        }

                        Log.e(
                            "postdata",
                            "${returnStartDay.value!![0]}-${returnStartDay.value!![1]}-${returnStartDay.value!![2]} ${returnStartHour.value!![0]}:${returnStartHour.value!![1]}"
                        )
                        Log.e(
                            "postdata",
                            "${returnEndDate.value!![0]}-${returnEndDate.value!![1]}-${returnEndDate.value!![2]} ${returnEndHour.value!![0]}:${returnEndHour.value!![1]}"
                        )
                        Log.e(
                            "postdata",
                            binding.txtTitle.text.toString() + "@^" + binding.txtMemo.text?.toString()
                        )

                    }
                    //등록완료하고 AlldayDots livedata바꿔줌
                    runBlocking {
                        myViewModel.getAlldayDots(requireActivity())
                        myViewModel.updateOneDaySchedule(requireActivity())
                    }
                    dismiss()


                }
            }
        }
        return binding.root
    }

    companion object {
        var enrollCoroutine: Int? = null
    }

}

