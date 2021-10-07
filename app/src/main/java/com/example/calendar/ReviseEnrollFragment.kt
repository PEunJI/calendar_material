package com.example.calendar

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.example.calendar.Picker.ReviseDatePicker
import com.example.calendar.Picker.ReviseTimePicker
import com.example.calendar.ScheduleEnrollFragment.Companion.isRightRange
import com.example.calendar.ScheduleEnrollFragment.Companion.startDToDate
import com.example.calendar.databinding.FragmentReviseEnrollBinding
import com.example.calendar.kakaoLogin.KakaoLogin
import com.example.calendar.kakaoLogin.MasterApplication
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.*
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
            main = ReviseEnrollFragment.Companion
        }

        //첫세팅
        start_liveDate_revise.value = "${startY}년 ${startM}월 ${startD}일 "
        start_liveHour_revise.value = "${startH}시 ${startm}분"
        end_liveDate_revise.value = "${endY}년 ${endM}월 ${endD}일 "
        end_liveHour_revise.value = "${endH}시 ${endm}분"
        binding.txtMemo.setText(StringBuffer(memo))
        binding.txtTitle.setText(StringBuffer(title))

        //cancle button
        binding.btnCancle.setOnClickListener {
            dismiss()
        }


        //return 변수 초기화

        returnStartDay_revise = arrayListOf(startY.toLong(), startM.toLong(), startD.toLong())
        returnEndDate_revise = arrayListOf(endY.toLong(), endM.toLong(), endD.toLong())
        returnStartHour_revise = arrayListOf(startH.toLong(), startm.toLong())
        returnEndHour_revise = arrayListOf(endH.toLong(), endm.toLong())


        //picker setting
        val date_picker =
            ReviseDatePicker(
                context = get_context,
                mYear = startY,
                mMonth = startM,
                mDay = startD
            )

        val hour_picker =
            ReviseTimePicker(
                 get_context
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
            var startDate = startDToDate(returnStartDay_revise, returnStartHour_revise)
            val date_picker =
                ReviseDatePicker(
                    context = get_context,
                    mYear = startY,
                    mMonth = startM,
                    mDay = startD
                )
            date_picker.endDatePickerDialog.apply {
                datePicker.minDate = (startDate.time)
            }.show()
        }


        //종료 시간 눌렀을 때 timepicker 띄우기
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
                isRightRange(start_liveDate_revise.value!!, start_liveHour_revise.value!!, end_liveDate_revise.value!!, end_liveHour_revise.value!!)
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
                                "${returnStartDay_revise[0]}-${returnStartDay_revise[1]}-${returnStartDay_revise[2]} ${returnStartHour_revise[0]}:${returnStartHour_revise[1]}"
                            input["dateEnd"] =
                                "${returnEndDate_revise[0]}-${returnEndDate_revise[1]}-${returnEndDate_revise[2]} ${returnEndHour_revise[0]}:${returnEndHour_revise[1]}"
                            input["content"] =
                                binding.txtTitle.text.toString() + "@^" + binding.txtMemo.text?.toString()

                            (requireActivity().application as MasterApplication).service.putCalendar(
                                "$id",
                                input
                            )

                        }
                        job.join()
                        //등록완료하고 AlldayDots livedata바꿔줌 & onedayschedule의 livedata도 바꿔줌
                        val job2 = CoroutineScope(Dispatchers.IO).async {
                            KakaoLogin.myViewModel.getAlldayDots(requireActivity())
                            KakaoLogin.myViewModel.updateOneDaySchedule(requireActivity())
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
        lateinit var returnStartDay_revise: ArrayList<Long>
        lateinit var returnEndDate_revise: ArrayList<Long>
        lateinit var returnStartHour_revise: ArrayList<Long>
        lateinit var returnEndHour_revise: ArrayList<Long>

        val start_liveDate_revise = MutableLiveData<String>()
        val start_liveHour_revise = MutableLiveData<String>()
        val end_liveDate_revise = MutableLiveData<String>()
        val end_liveHour_revise = MutableLiveData<String>()

    }

}