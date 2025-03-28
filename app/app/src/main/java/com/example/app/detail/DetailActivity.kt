package com.example.app.detail

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
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

    private var isFragmentVisible = false // 프래그먼트가 보이는지 상태 추적


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 상단
        // 뒤로가기
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
        // 출발역 : 역이름1 - 출발역 정보 받아야함

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
        // 도착역 : 역이름2 - 출발역 정보 받아야함
        binding.btnDetailArrival1.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        // 즐겨찾기 - 즐겨찾기 페이지로 해당 정보 넘길 수 있도록  추가 작성(보류)
        binding.btnDetailHotlist.setOnClickListener{
            // 즐겨찾기 누르면 색상 변경
            if (isblue){
                binding.btnDetailHotlist.clearColorFilter()
            }
            else{
                binding.btnDetailHotlist.setColorFilter(0xFF00AAFF.toInt(), PorterDuff.Mode.SRC_IN)
            }
            isblue = !isblue  // 상태 토글
        }

        // 평일, 토요일, 공휴일 선택
        // 매인 상단 버튼
        // 초기 화면에서 정보 보이도록 설정
        binding.detailInfoLayout.visibility = View.VISIBLE
        binding.timePickerContainer.visibility = View.GONE
        // 평일, 토요일, 공휴일 선택 - 프래그먼트
        binding.btnOp1.setOnClickListener {
            // 기존 정보 숨기기
            // binding.detailInfoLayout.visibility = View.GONE

            // DaySelectionFragment로 화면 전환
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
            // 출발시간, 도착시간, 막차시간을 선택할 다이얼로그를 띄움
            openTimeSelectionDialog()
        }

        // 최소시간/ 최소환승 선택 버튼 클릭 시
        binding.btnOp4.setOnClickListener {
            // 최소시간/ 최소환승 다이얼로그 열기
            val fragment = MinTimeDialogFragment()
            fragment.show(supportFragmentManager, "minTimeDialog")
        }


// 출발역과 도착역 정보를 전달하면서 StartBtnFragment 열기
        // 메인 정보
        // 종착역 : 정보 필요 - id: last_train
        // 출발역 : 다이얼로그로 표시 - 출발역 호출
        binding.btnDetailStart2.setOnClickListener {
            val startBtnFragment = StartBtnFragment()
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
        // 호선 : 호선별 색상 변경 - id: detail_line
        // 도착역 : 다이얼로그로 표시 - 도착역 호출
        binding.btnDetailArrival2.setOnClickListener {
            val arrivalBtnFragment = ArrivalBtnFragment()
            arrivalBtnFragment.show(supportFragmentManager, "arrivalBtnFragment")
        }
        // 출발시간 : 출발 시간 필요 - id: detail_start
        // 도착시간 : 도착 시간 필요 - id: detail_arrival

        // 하단
        // 소요시간 : 소요시간 정보 필요 - id: use_time
        // 경유 : 경우 갯수 정보 필요 - id: station_number
        // ! 버튼 - 요금정보
        binding.rateInformation.setOnClickListener{
           val intent = Intent(this, RateInformationActivity::class.java)
            startActivity(intent)
        }

        binding.btnDetailStart1.text = arrivalText
        binding.btnDetailArrival1.text = startText
        binding.btnDetailStart2.text = arrivalText
        binding.btnDetailArrival2.text = startText
    }
        // 하단 버튼
        // 이전열차 : 버튼 클릭시 정보 변경 필요 - id: previous_train
        // 경유보기 : 노선 이미지 확대방법 찾아보기
        binding.btnRoute.setOnClickListener {
            val intent = Intent(this, RouteActivity::class.java)
            startActivity(intent)
        }
        // 하차알람: 해당 화면 도착시간 고정된 상태
        binding.btnGetOff.setOnClickListener {
            val intent = Intent(this, GetOffActivity::class.java)
            startActivity(intent)
        }
        // 다음열차 : 버튼 클릭시 정보 변경 필요 - id: next_train

    // MainActivity로 돌아가기
    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


    // 메인 상단 버튼 - 시간
    // 시간 설정 화면을 띄우는 메소드
    private fun openTimePickerFragment() {
        val timePickerFragment = TimePickerFragment()
        timePickerFragment.show(supportFragmentManager, "timePicker")

        toggleDetailVisibility(isVisible = false)
        // 정보 영역 숨기기
        binding.detailInfoLayout.visibility = View.GONE
        // 시간 설정 화면을 표시할 컨테이너
        binding.timePickerContainer.visibility = View.VISIBLE
    }

    // 시간 설정 완료 후 호출되는 메소드
    fun onTimeSet(time: String) {
        selectedTime = time
        binding.btnOp2.text = selectedTime
        toggleDetailVisibility(isVisible = true)
    }

         //정보 영역 다시 보이기
        binding.detailInfoLayout.visibility = View.VISIBLE

         //시간 설정 화면 숨기기
        binding.timePickerContainer.visibility = View.GONE
    }
    // 취소 시 호출되어 정보 영역을 다시 보이게 하는 메소드
    fun restoreInfoLayoutVisibility() {
        toggleDetailVisibility(isVisible = true)
    }

    // 출발시간, 도착시간, 막차시간을 선택할 다이얼로그 띄우기

    // 메인 상단 버튼 - 출발시간
    // 출발시간, 도착시간, 막차시간을 선택할 다이얼로그 프래그먼트 띄우기
    private fun openTimeSelectionDialog() {
        val timeSelectionFragment = TimeSelectionDialogFragment()
        timeSelectionFragment.show(supportFragmentManager, "timeSelectionDialog")
        toggleDetailVisibility(isVisible = false)
        // 정보 영역 숨기기
        //binding.detailInfoLayout.visibility = View.GONE
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


