package com.example.app.detail

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import com.example.app.R
import com.example.app.databinding.ActivityDetailBinding
import com.example.app.databinding.FragmentBlank1Binding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment1.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankFragment1 : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var btnOp1: Button
    lateinit var radioGroup: RadioGroup
    lateinit var radioWeekday: RadioButton
    lateinit var radioSaturday: RadioButton
    lateinit var radioHoliday: RadioButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // DialogFragment의 스타일 설정 (필요 시 다이얼로그 크기, 스타일 등을 설정 가능)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_blank1, container, false)

        btnOp1 = activity?.findViewById(R.id.btn_op1)!!
        radioGroup = view.findViewById(R.id.radioGroupDays)
        radioWeekday = view.findViewById(R.id.radio_weekday)
        radioSaturday = view.findViewById(R.id.radio_saturday)
        radioHoliday = view.findViewById(R.id.radio_holiday)

        // 라디오 버튼 선택 리스너 설정
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_weekday -> btnOp1.text = "평일"
                R.id.radio_saturday -> btnOp1.text = "토요일"
                R.id.radio_holiday -> btnOp1.text = "공휴일"
            }

            // 기존 정보 화면 보이기
            val detailInfoLayout: LinearLayout = activity?.findViewById(R.id.detail_info_layout)!!
            detailInfoLayout.visibility = View.VISIBLE

            // 다이얼로그 닫기
            dismiss()
        }

        return view
    }
    // 다이얼로그 크기 및 위치 설정
    override fun onStart() {
        super.onStart()
        val dialog = dialog
        val window = dialog?.window
        val params = window?.attributes

        // 화면의 반으로 크기 설정
        params?.width = (resources.displayMetrics.widthPixels * 0.7).toInt() // 80% 크기로 설정

        // 화면의 가운데 배치
        params?.height = (resources.displayMetrics.heightPixels * 0.4).toInt() // 50% 높이로 설정
        window?.attributes = params
        window?.setGravity(Gravity.CENTER) // 화면의 중앙에 배치
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment1.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BlankFragment1().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}