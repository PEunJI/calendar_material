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
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ScheduleEnrollFragment : BottomSheetDialogFragment() {

    //현재 날짜
    val c: Calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
    var mYear: Int = 0
    var mMonth: Int = 0
    var mDay: Int = 0
    val mHour: Int = c.get(Calendar.HOUR_OF_DAY)
    val mMinute: Int = c.get(Calendar.MINUTE)


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

        Log.e("datecheckeeeee",mDay.toString())
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
            main = ScheduleEnrollFragment.Companion
        }

        //첫세팅 (누른날짜~누시간+1시간)
        start_liveDate.value = "${mYear}년 ${mMonth}월 ${mDay}일 "
        start_liveHour.value = "${mHour}시 ${mMinute}분"
        end_liveDate.value = "${mYear}년 ${mMonth}월 ${mDay}일 "
        end_liveHour.value = "${mHour + 1}시 ${mMinute}분"
        Log.d("dateTest2", "${mMonth}월")

        returnStartDay = arrayListOf(mYear.toLong(), mMonth.toLong(), mDay.toLong())
        returnEndDate = arrayListOf(mYear.toLong(), mMonth.toLong(), mDay.toLong())
        returnStartHour = arrayListOf(mHour.toLong(), mMinute.toLong())
        returnEndHour = arrayListOf(mHour.toLong() + 1, mMinute.toLong())


        //cancle button
        binding.btnCancle.setOnClickListener {
            dismiss()
        }

        //picekr setting
        val date_picker =
            DatePicker(
                get_context,
                mYear,
                mMonth,
                mDay
            )

        val hour_picker =
            TimePicker(
                get_context,
                mHour,
                mMinute
            )


        //시작 날짜 눌렀을 때 datepicker띄우기
        binding.startDatePicker.setOnClickListener {
            date_picker.startDatePickerDialog.show()

        }

        //시작 시간 눌렀을 때 timepicker띄우기
        binding.startTimePicker.setOnClickListener {
            hour_picker.startTimePickerDialog.apply {
                this.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            }.show()

        }

        //종료 날짜 눌렀을 때 datepicker띄우기
        binding.endDatePicker.setOnClickListener {
            var startDate = startDToDate(returnStartDay, returnStartHour)
            val date_picker =
                DatePicker(
                    get_context,
                    mYear,
                    mMonth,
                    mDay
                )
            date_picker.endDatePickerDialog.apply {
                datePicker.minDate = (startDate.time)
            }.show()
        }


        //종료 시간은 시작 시간 이후로만 선택되게 하고 싶지만 커스텀해야하는 것 같다. 구찮다. 그냥 토스트를 띄우고 스타트타임+1시간으로 고정시키자
        binding.endTimePicker.setOnClickListener {

            hour_picker.timePickerDialog_end.show()

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
                isRightRange(
                    start_liveDate.value!!,
                    start_liveHour.value!!,
                    end_liveDate.value!!,
                    end_liveHour.value!!
                )
            ) {
                Toast.makeText(get_context, "시작시간은 종료시간 전이여야 합니다.", Toast.LENGTH_SHORT).show()
            } else {
                //제목이 null일때
                if (binding.txtTitle.text.toString() == "") {
                    Toast.makeText(get_context, "제목은 필수입니다.", Toast.LENGTH_SHORT).show()
                } else {
                    //등록
                    runBlocking {

                        val job = CoroutineScope(Dispatchers.IO).async {
                            input["dateStart"] =
                                "${returnStartDay[0]}-${returnStartDay[1]}-${returnStartDay[2]} ${returnStartHour[0]}:${returnStartHour[1]}"

                            //23시에 등록하면 다음날 24시로 등록되서 오류나는 부분 해결 (다음날 00시로 명시)
                            if (returnEndHour[0] != 24L) {
                                input["dateEnd"] =
                                    "${returnEndDate[0]}-${returnEndDate[1]}-${returnEndDate[2]} ${returnEndHour[0]}:${returnEndHour[1]}"
                            } else {

                                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
                                val next_date =
                                    formatter.parse("${returnEndDate[0]}-${returnEndDate[1]}-${returnEndDate[2]} ${returnEndHour[0]}:${returnEndHour[1]}")
                                val cal = Calendar.getInstance()
                                cal.time = next_date
                                // cal.add(java.util.Calendar.DAY_OF_MONTH, 1)


                                input["dateEnd"] =
                                    "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.MONTH) + 1}-${
                                        cal.get(
                                            Calendar.DATE
                                        )
                                    } 00:${cal.get(Calendar.MINUTE)}"
                            }

                            input["content"] =
                                binding.txtTitle.text.toString() + "@^" + binding.txtMemo.text?.toString()
                            (requireActivity().application as MasterApplication).service.postCalendar(
                                input
                            )
                        }



                        job.join()
                        //등록완료하고 AlldayDots livedata바꿔줌 & onedayschedule의 livedata도 바꿔줌
                        val job2 = CoroutineScope(Dispatchers.IO).async {
                            myViewModel.getAlldayDots(requireActivity())
                            myViewModel.updateOneDaySchedule(requireActivity())
                        }
                        job2.join()
                    }
                    dismiss()
                }
            }

        }
        return binding.root

    }

    companion object {

        //return 변수 초기화
        lateinit var returnStartDay: ArrayList<Long>
        lateinit var returnEndDate: ArrayList<Long>
        lateinit var returnStartHour: ArrayList<Long>
        lateinit var returnEndHour: ArrayList<Long>

        val start_liveDate = MutableLiveData<String>()
        val start_liveHour = MutableLiveData<String>()
        val end_liveDate = MutableLiveData<String>()
        val end_liveHour = MutableLiveData<String>()


        fun isRightRange(startD: String, startH: String, endD: String, endH: String): Boolean {

            val start = (startD + startH).replace(
                ("[^\\d.]").toRegex(),
                ""
            ).toLong()

            val end = (endD + endH).replace(
                ("[^\\d.]").toRegex(),
                ""
            ).toLong()

            val boolean = start > end

            return boolean
        }

        fun startDToDate(startD: ArrayList<Long>, startH: ArrayList<Long>): Date {
            //종료 날짜는 시작날짜 이후로만 선택되게
            //날짜 String->dateFormat->TimeMillis()
            var date =
                "${startD[0]}-${startD[1]}-${startD[2]} ${startH[0]}:${startH[1]}"
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
            return simpleDateFormat.parse(date)

        }
    }

}

