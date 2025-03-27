package com.example.app.detail

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app.MainActivity
import com.example.app.R
import com.example.app.databinding.ActivityDetailBinding

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

        // Intent로부터 데이터를 받아오기
        val departure = intent.getStringExtra("departure")
        val arrival = intent.getStringExtra("arrival")

        // 받아온 데이터를 화면에 표시
        binding.btnDetailStart1.text = "$departure"
        binding.btnDetailArrival1.text = "$arrival"

        binding.btnDetailStart2.text = "$departure"
        binding.btnDetailArrival2.text = "$arrival"


        // 상단 뒤로가기 버튼
        binding.btnDetailBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        // 상단 출발 역이름 버튼 - 역이름 불러와야함
        binding.btnDetailStart1.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        // 상단 역위치 변경 버튼 - 역위치 변경시 내용 변경될 수 있도록 작성
        binding.btnDetailChange.setOnClickListener {
            // btn_detail_start1과 btn_detail_arrival1 버튼의 텍스트를 서로 교환
            val startText = binding.btnDetailStart1.text.toString()
            val arrivalText = binding.btnDetailArrival1.text.toString()
            val startText3 = binding.btnDetailStart2.text.toString()
            val arrivalText3 = binding.btnDetailArrival2.text.toString()

            // 텍스트 교환
            binding.btnDetailStart1.text = arrivalText
            binding.btnDetailArrival1.text = startText
            binding.btnDetailStart2.text = arrivalText3
            binding.btnDetailArrival2.text = startText3
        }
        // 상단 도착 역이름 버튼 - 역이름 불러와야함
        binding.btnDetailArrival1.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        // 상단 즐겨찾기 버튼 - 즐겨찾기 페이지로 해당 정보 넘길 수 있도록  추가 작성
        binding.btnDetailHotlist.setOnClickListener{
            if (isblue){
                binding.btnDetailHotlist.clearColorFilter()
            }
            else{
                binding.btnDetailHotlist.setColorFilter(0xFF00AAFF.toInt(), PorterDuff.Mode.SRC_IN)
            }
            isblue = !isblue  // 상태 토글
        }

        // 초기 화면에서 정보 보이도록 설정
        binding.detailInfoLayout.visibility = View.VISIBLE
        binding.timePickerContainer.visibility = View.GONE
        // 평일, 토요일, 공휴일 선택
        binding.btnOp1.setOnClickListener {
            // 기존 정보 숨기기
            binding.detailInfoLayout.visibility = View.GONE

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

        // btn_detail_start2 버튼 클릭 시 StartBtnFragment를 다이얼로그로 표시
        binding.btnDetailStart2.setOnClickListener {
            val startBtnFragment = StartBtnFragment()
            startBtnFragment.show(supportFragmentManager, "startBtnFragment")
        }






    }
    // 시간 설정 화면을 띄우는 메소드
    private fun openTimePickerFragment() {
        val timePickerFragment = TimePickerFragment()
        timePickerFragment.show(supportFragmentManager, "timePicker")

        // 정보 영역 숨기기
        binding.detailInfoLayout.visibility = View.GONE

        // 시간 설정 화면을 표시할 컨테이너
        binding.timePickerContainer.visibility = View.VISIBLE
    }
    // 시간 설정 완료 후 호출되는 메소드
    fun onTimeSet(time: String) {
        // 시간 설정 후 버튼에 시간 표시
        selectedTime = time
        binding.btnOp2.text = selectedTime

        // 정보 영역 다시 보이기
        binding.detailInfoLayout.visibility = View.VISIBLE

        // 시간 설정 화면 숨기기
        binding.timePickerContainer.visibility = View.GONE
    }
    // 취소 시 호출되어 정보 영역을 다시 보이게 하는 메소드
    fun restoreInfoLayoutVisibility() {
        binding.detailInfoLayout.visibility = View.VISIBLE
        binding.timePickerContainer.visibility = View.GONE
    }
    // 출발시간, 도착시간, 막차시간을 선택할 다이얼로그 프래그먼트 띄우기
    private fun openTimeSelectionDialog() {
        val timeSelectionFragment = TimeSelectionDialogFragment()
        timeSelectionFragment.show(supportFragmentManager, "timeSelectionDialog")
        // 정보 영역 숨기기
        binding.detailInfoLayout.visibility = View.GONE
    }
    // 다이얼로그에서 선택된 시간 설정 후 호출되는 메소드
    fun onTimeSelected(time: String) {
        selectedTime = time
        binding.btnOp3.text = selectedTime // 버튼 텍스트 업데이트
        // 정보 영역 다시 보이기
        binding.detailInfoLayout.visibility = View.VISIBLE
    }
    // 선택된 최소시간/ 최소환승을 btnOp4에 반영
    fun onMinTimeSelected(option: String) {
        binding.btnOp4.text = option
    }


}


