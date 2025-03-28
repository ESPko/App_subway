package com.example.app.detail

import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.app.R
import com.example.app.databinding.ActivityDetailBinding
import com.example.app.databinding.FragmentTimePickerBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TimePickerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimePickerFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentTimePickerBinding  // ViewBinding을 위한 변수 선언


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // ViewBinding 초기화
        binding = FragmentTimePickerBinding.inflate(inflater, container, false)

        // 시간 설정 버튼
        binding.btnConfirm.setOnClickListener {
            val hour = binding.timePicker.hour
            val minute = binding.timePicker.minute
            val time = String.format("%02d:%02d", hour, minute)

            // Activity로 시간 전달
            (activity as? DetailActivity)?.onTimeSet(time)

            dismiss() // 다이얼로그 닫기
        }

        // 취소 버튼
        binding.btnCancel.setOnClickListener {
            dismiss() // 다이얼로그 닫기
        }

        return binding.root
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        // 다이얼로그가 닫힌 후 정보 영역을 다시 보이게 하기
        (activity as? DetailActivity)?.restoreInfoLayoutVisibility()
    }
    }

private fun DetailActivity?.restoreInfoLayoutVisibility() {
    TODO("Not yet implemented")
}

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment TimePickerFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            TimePickerFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
//}
