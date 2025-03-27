package com.example.app.detail

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app.MainActivity
import com.example.app.R
import com.example.app.databinding.ActivityDetailBinding
import com.example.app.dto.CategoryDTO

class DetailActivity : AppCompatActivity() {

    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    // 즐겨찾기 상태를 추적하기 위한 변수
    private var isblue = false

    // 시간 설정
    private var selectedTime = "09:05" // 예시로 설정된 기본 시간

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Intent로부터 CategoryDTO 객체를 받아오기
        val departure = intent.getParcelableExtra<CategoryDTO>("departure")
        val arrival = intent.getParcelableExtra<CategoryDTO>("arrival")

        // 출발역과 도착역 정보를 화면에 표시
        departure?.let {
            binding.btnDetailStart1.text = it.name
            binding.btnDetailStart2.text = it.name
        }
        arrival?.let {
            binding.btnDetailArrival1.text = it.name
            binding.btnDetailArrival2.text = it.name
        }

        // lineColor 설정
        // departure?.line이 null인 경우 기본값 "defaultLine"을 제공
        setLineColor(departure?.line ?: "defaultLine")

        // 상단 뒤로가기 버튼
        binding.btnDetailBack.setOnClickListener {
            navigateToMainActivity()
        }

        // 상단 출발 역이름 버튼
        binding.btnDetailStart1.setOnClickListener {
            navigateToMainActivity()
        }

        // 상단 역위치 변경 버튼
        binding.btnDetailChange.setOnClickListener {
            switchStations()
        }

        // 즐겨찾기 버튼
        binding.btnDetailHotlist.setOnClickListener {
            toggleFavorite()
        }

        // 평일, 토요일, 공휴일 선택
        binding.btnOp1.setOnClickListener {
            val fragment = BlankFragment1()
            fragment.show(supportFragmentManager, "blankFragment")
        }

        // 시간 버튼 클릭 시 시간 설정 화면 열기
        binding.btnOp2.text = selectedTime
        binding.btnOp2.setOnClickListener {
            openTimePickerFragment()
        }

        // 출발시간 버튼 클릭
        binding.btnOp3.setOnClickListener {
            openTimeSelectionDialog()
        }

        // 최소시간/ 최소환승 선택 버튼 클릭 시
        binding.btnOp4.setOnClickListener {
            val fragment = MinTimeDialogFragment()
            fragment.show(supportFragmentManager, "minTimeDialog")
        }


// 출발역과 도착역 정보를 전달하면서 StartBtnFragment 열기
        binding.btnDetailStart2.setOnClickListener {
            val startBtnFragment = StartBtnFragment.newInstance(
                departure ?: CategoryDTO("", "", ""),  // 기본 값으로 초기화
                arrival ?: CategoryDTO("", "", "")     // 기본 값으로 초기화
            )
            startBtnFragment.show(supportFragmentManager, "startBtnFragment")
        }
    }

    private fun setLineColor(line: String?) {
        val lineColorView = binding.lineColor
        val lineColor = when (line) {
            "1" -> R.color.line1Color  // 1호선 색상
            "2" -> R.color.line2Color  // 2호선 색상
            "3" -> R.color.line3Color  // 3호선 색상
            "4" -> R.color.line4Color  // 4호선 색상
            else -> R.color.defaultColor // 기본 색상
        }
        // 리소스 색상을 가져와서 배경 색상 설정
        lineColorView.setBackgroundColor(ContextCompat.getColor(this, lineColor))
    }

    // 즐겨찾기 상태 토글
    private fun toggleFavorite() {
        if (isblue) {
            binding.btnDetailHotlist.clearColorFilter()
        } else {
            binding.btnDetailHotlist.setColorFilter(0xFF00AAFF.toInt(), PorterDuff.Mode.SRC_IN)
        }
        isblue = !isblue
    }

    // 출발역과 도착역 텍스트 교환
    private fun switchStations() {
        val startText = binding.btnDetailStart1.text.toString()
        val arrivalText = binding.btnDetailArrival1.text.toString()


        binding.btnDetailStart1.text = arrivalText
        binding.btnDetailArrival1.text = startText
        binding.btnDetailStart2.text = arrivalText
        binding.btnDetailArrival2.text = startText
    }

    // MainActivity로 돌아가기
    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    // 시간 설정 화면을 띄우는 메소드
    private fun openTimePickerFragment() {
        val timePickerFragment = TimePickerFragment()
        timePickerFragment.show(supportFragmentManager, "timePicker")

        toggleDetailVisibility(isVisible = false)
    }

    // 시간 설정 완료 후 호출되는 메소드
    fun onTimeSet(time: String) {
        selectedTime = time
        binding.btnOp2.text = selectedTime
        toggleDetailVisibility(isVisible = true)
    }

    // 취소 시 호출되어 정보 영역을 다시 보이게 하는 메소드
    fun restoreInfoLayoutVisibility() {
        toggleDetailVisibility(isVisible = true)
    }

    // 출발시간, 도착시간, 막차시간을 선택할 다이얼로그 띄우기
    private fun openTimeSelectionDialog() {
        val timeSelectionFragment = TimeSelectionDialogFragment()
        timeSelectionFragment.show(supportFragmentManager, "timeSelectionDialog")
        toggleDetailVisibility(isVisible = false)
    }

    // 다이얼로그에서 선택된 시간 설정 후 호출되는 메소드
    fun onTimeSelected(time: String) {
        selectedTime = time
        binding.btnOp3.text = selectedTime
        toggleDetailVisibility(isVisible = true)
    }

    // 최소시간/ 최소환승을 btnOp4에 반영
    fun onMinTimeSelected(option: String) {
        binding.btnOp4.text = option
    }

    // 정보 영역의 가시성 상태 관리
    private fun toggleDetailVisibility(isVisible: Boolean) {
        if (isVisible) {
            binding.detailInfoLayout.visibility = View.VISIBLE
            binding.timePickerContainer.visibility = View.GONE
        } else {
            binding.detailInfoLayout.visibility = View.GONE
            binding.timePickerContainer.visibility = View.VISIBLE
        }
    }
}
