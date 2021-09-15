package com.example.calendar

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.example.calendar.Adapter.ScheduleList
import com.example.calendar.Picker.DatePicker
import com.example.calendar.Picker.ReviseDatePicker
import com.example.calendar.Picker.ReviseTimePicker
import com.example.calendar.Picker.TimePicker
import com.example.calendar.Retrofit.RetrofitService
import com.example.calendar.databinding.FragmentReviseEnrollBinding
import com.example.calendar.databinding.FragmentScheduleEnrollBinding
import com.example.calendar.kakaoLogin.KakaoLogin
import com.example.calendar.kakaoLogin.KakaoSDKInit
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ReviseEnrollFragment : BottomSheetDialogFragment() {

    //현재 날짜
    var startY: Int = 0
    var startM: Int = 0
    var startD: Int = 0
    var startH: Int = 0
    var startm: Int = 0
    var endY: Int = 0
    var endM: Int = 0
    var endD: Int = 0
    var endH: Int = 0
    var endm: Int = 0
    var memo: String = ""
    var title: String = ""
    var id: Int? = null

    val start_liveDate = MutableLiveData<String>()
    val start_liveHour = MutableLiveData<String>()
    val end_liveDate = MutableLiveData<String>()
    val end_liveHour = MutableLiveData<String>()

    lateinit var get_context: Activity
    lateinit var binding: FragmentReviseEnrollBinding
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            get_context = context as Activity
        }

        startY = arguments?.getString("startY")!!.toInt()
        startM = arguments?.getString("startM")!!.toInt()
        startD = arguments?.getString("startD")!!.toInt()
        startH = arguments?.getString("startH")!!.toInt()
        startm = arguments?.getString("startm")!!.toInt()
        endY = arguments?.getString("endY")!!.toInt()
        endM = arguments?.getString("endM")!!.toInt()
        endD = arguments?.getString("endD")!!.toInt()
        endH = arguments?.getString("endH")!!.toInt()
        endm = arguments?.getString("endm")!!.toInt()
        try {
            memo = arguments?.getString("memo")!!.toString()
        } catch (e: Exception) {
        }
        title = arguments?.getString("title")!!.toString()
        id = arguments?.getInt("id")


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
            DataBindingUtil.inflate(inflater, R.layout.fragment_revise_enroll, container, false)
        binding.apply {
            lifecycleOwner = this@ReviseEnrollFragment
            main = this@ReviseEnrollFragment
        }

        //첫세팅
        start_liveDate.value = "${startY}년 ${startM}월 ${startD}일 "
        start_liveHour.value = "${startH}시 ${startm}분"
        end_liveDate.value = "${endY}년 ${endM}월 ${endD}일 "
        end_liveHour.value = "${endH}시 ${endm}분"
        binding.txtMemo.setText(StringBuffer(memo))
        binding.txtTitle.setText(StringBuffer(title))

        //cancle button
        binding.btnCancle.setOnClickListener {
            dismiss()
        }


        //return 변수 초기화
        var returnStartDay = MutableLiveData<Array<Long>>()
        var returnEndDate = MutableLiveData<Array<Long>>()
        var returnStartHour = MutableLiveData<Array<Long>>()

        returnStartDay.value = arrayOf(startY.toLong(), startM.toLong(), startD.toLong())
        returnEndDate.value = arrayOf(endY.toLong(), endM.toLong(), endD.toLong())
        returnStartHour.value = arrayOf(startH.toLong(), startm.toLong())
        returnEndHour.value = arrayOf(endH.toLong(), endm.toLong())


        //시작 날짜 피커
        val startdate_picker =
            ReviseDatePicker(
                mutableLiveData = start_liveDate,
                returnStartDay = returnStartDay,
                context = get_context,
                mYear = startY,
                mMonth = startM,
                mDay = startD,
                mutableLiveData_end = end_liveDate
            )
        //종료날짜피커
        val enddate_picker =
            ReviseDatePicker(
                mutableLiveData = end_liveDate,
                returnStartDay = returnEndDate,
                context = get_context,
                mYear = endY,
                mMonth = endM,
                mDay = endD
            )
        //시작 시간 피커
        val starthour_picker =
            ReviseTimePicker(
                mutableLiveData = start_liveHour,
                context = get_context,
                mutableStartDate = returnStartDay,
                mutableEndDate = returnEndDate,
                mutableStartHour = returnStartHour
            )

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

        val endhour_picker = ReviseTimePicker(
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
                    GlobalScope.launch {
                        input["dateStart"] =
                            "${returnStartDay.value!![0]}-${returnStartDay.value!![1]}-${returnStartDay.value!![2]} ${returnStartHour.value!![0]}:${returnStartHour.value!![1]}"
                       Log.e("putcalendar",input["dateStart"].toString())
                        input["dateEnd"] =
                            "${returnEndDate.value!![0]}-${returnEndDate.value!![1]}-${returnEndDate.value!![2]} ${returnEndHour.value!![0]}:${returnEndHour.value!![1]}"
                        Log.e("putcalendar",input["dateEnd"].toString())
                        input["content"] =
                            binding.txtTitle.text.toString() + "@^" + binding.txtMemo.text?.toString()
                        Log.e("putcalendar",input["content"].toString())
                        (requireActivity().application as KakaoSDKInit).service.putCalendar("$id","${KakaoLogin.user_id}", input)
                    }
                    dismiss()
                }
            }
        }

        return binding.root



    }



    companion object{
        var returnEndHour = MutableLiveData<Array<Long>>()

    }

}