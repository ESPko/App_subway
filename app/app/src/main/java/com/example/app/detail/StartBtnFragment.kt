package com.example.app.detail

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.app.R
import com.example.app.databinding.FragmentStartBtnBinding
import com.example.app.dto.CategoryDTO

class StartBtnFragment : DialogFragment() {

    private lateinit var binding: FragmentStartBtnBinding

    // 전달받은 데이터를 저장할 변수
    private var startStation: String? = null  // 출발역 이름
    private var endStation: String? = null    // 도착역 이름
    private var line: Int? = null             // 출발역 라인 정보 (Int?로 유지)

    companion object {
        // 새로운 인스턴스를 생성할 때 전달된 데이터를 받을 상수
        private const val ARG_START_STATION = "startStation"
        private const val ARG_END_STATION = "endStation"
        private const val ARG_LINE = "line"

        // CategoryDTO를 받아서 새로운 StartBtnFragment를 생성하는 메서드
        @JvmStatic
        fun newInstance(departure: CategoryDTO, arrival: CategoryDTO) =
            StartBtnFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_START_STATION, departure.name)  // 출발역 이름
                    putString(ARG_END_STATION, arrival.name)      // 도착역 이름
                    putInt(ARG_LINE, departure.line ?: 1)         // 출발역 라인 정보 (Int?로 처리)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 전달된 데이터가 있을 경우, arguments에서 받아오기
        arguments?.let {
            startStation = it.getString(ARG_START_STATION)  // 출발역 이름
            endStation = it.getString(ARG_END_STATION)      // 도착역 이름
            line = it.getInt(ARG_LINE)                       // 출발역 라인 정보 (Int?로 처리)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 프래그먼트 레이아웃 설정
        binding = FragmentStartBtnBinding.inflate(inflater, container, false)

        // 출발역과 도착역 텍스트 설정
        binding.startStationTextView.text = startStation ?: "출발역"
        binding.endStationTextView.text = endStation ?: "도착역"

        // 출발역 라인에 따른 색상 설정
        setLineBackgroundColor(line)

        return binding.root
    }

    // 출발역 라인에 따른 배경 색상을 설정하는 메소드
    private fun setLineBackgroundColor(line: Int?) {
        val lineBackgroundView = binding.lineBackground
        when (line) {
            1 -> lineBackgroundView.setBackgroundColor(resources.getColor(R.color.line1Color))
            2 -> lineBackgroundView.setBackgroundColor(resources.getColor(R.color.line2Color))
            3 -> lineBackgroundView.setBackgroundColor(resources.getColor(R.color.line3Color))
            4 -> lineBackgroundView.setBackgroundColor(resources.getColor(R.color.line4Color))
            else -> lineBackgroundView.setBackgroundColor(resources.getColor(R.color.defaultColor))
        }
    }

    // 다이얼로그 크기 설정
    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (resources.displayMetrics.heightPixels / 2) // 세로 크기를 화면의 절반으로 설정
            )
            val dialogWindow = dialog.window
            dialogWindow?.setGravity(Gravity.BOTTOM) // 다이얼로그 위치를 화면 하단으로 설정
        }
    }
}
