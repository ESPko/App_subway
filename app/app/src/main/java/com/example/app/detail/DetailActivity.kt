package com.example.app.detail

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
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

    private var isFavoriteActive = false
    private var selectedTime = "09:05"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 초기 화면에서 정보 보이도록 설정
        binding.detailInfoLayout.visibility = View.VISIBLE
        binding.timePickerContainer.visibility = View.GONE

        // Intent로부터 CategoryDTO 객체들을 받아오기
        val selectedDeparture: CategoryDTO? = intent.getParcelableExtra("departure")
        val selectedArrival: CategoryDTO? = intent.getParcelableExtra("arrival")

        Log.d("csy", "Received departure: $selectedDeparture, arrival: $selectedArrival")

        // 출발역과 도착역 정보를 화면에 표시
        selectedDeparture?.let {
            binding.btnDetailStart1.text = it.name
            binding.btnDetailStart2.text = it.name
        }
        selectedArrival?.let {
            binding.btnDetailArrival1.text = it.name
            binding.btnDetailArrival2.text = it.name
        }

        // 출발역의 lineColor 설정
        selectedDeparture?.let {
            setLineColor(it.line)
        }

        // 상단 뒤로가기 버튼
        binding.btnDetailBack.setOnClickListener {
            navigateToMainActivity()
        }

        // 출발역 / 도착역 버튼 클릭
        binding.btnDetailStart1.setOnClickListener {
            navigateToMainActivity()
        }
        binding.btnDetailArrival1.setOnClickListener {
            navigateToMainActivity()
        }

        // 역위치 변경 버튼
        binding.btnDetailChange.setOnClickListener {
            switchStations()
        }

        // 즐겨찾기 버튼
        binding.btnDetailHotlist.setOnClickListener {
            toggleFavorite()
        }

        // 평일, 토요일, 공휴일 선택 - 프래그먼트
        binding.btnOp1.setOnClickListener {
            val fragment = BlankFragment1()
            fragment.show(supportFragmentManager, "blankFragment")
        }

        // 시간 설정 버튼
        binding.btnOp2.text = selectedTime
        binding.btnOp2.setOnClickListener {
            openTimePickerFragment()
        }

        // 출발시간, 도착시간, 막차시간을 선택할 다이얼로그
        binding.btnOp3.setOnClickListener {
            openTimeSelectionDialog()
        }

        // 최소시간/ 최소환승 선택 버튼 클릭
        binding.btnOp4.setOnClickListener {
            val fragment = MinTimeDialogFragment()
            fragment.show(supportFragmentManager, "minTimeDialog")
        }

        // 출발역과 도착역 정보를 전달하면서 StartBtnFragment 열기
        binding.btnDetailStart2.setOnClickListener {
            val startBtnFragment = StartBtnFragment()
            startBtnFragment.show(supportFragmentManager, "startBtnFragment")
        }
    }

    private fun setLineColor(line: Int?) {
        val lineColorView = binding.lineColor
        val lineColor = when (line) {
            1 -> R.color.line1Color
            2 -> R.color.line2Color
            3 -> R.color.line3Color
            4 -> R.color.line4Color
            else -> R.color.defaultColor
        }
        lineColorView.setBackgroundColor(ContextCompat.getColor(this, lineColor))
    }

    private fun toggleFavorite() {
        if (isFavoriteActive) {
            binding.btnDetailHotlist.clearColorFilter()
        } else {
            binding.btnDetailHotlist.setColorFilter(0xFF00AAFF.toInt(), PorterDuff.Mode.SRC_IN)
        }
        isFavoriteActive = !isFavoriteActive
    }

    private fun switchStations() {
        val startText = binding.btnDetailStart1.text.toString()
        val arrivalText = binding.btnDetailArrival1.text.toString()

        binding.btnDetailStart1.text = arrivalText
        binding.btnDetailArrival1.text = startText
        binding.btnDetailStart2.text = arrivalText
        binding.btnDetailArrival2.text = startText
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun openTimePickerFragment() {
        val timePickerFragment = TimePickerFragment()
        timePickerFragment.show(supportFragmentManager, "timePicker")
        toggleDetailVisibility(isVisible = false)
    }

    fun onTimeSet(time: String) {
        selectedTime = time
        binding.btnOp2.text = selectedTime
        toggleDetailVisibility(isVisible = true)
    }

    private fun openTimeSelectionDialog() {
        val timeSelectionFragment = TimeSelectionDialogFragment()
        timeSelectionFragment.show(supportFragmentManager, "timeSelectionDialog")
        toggleDetailVisibility(isVisible = false)
    }

    fun onTimeSelected(time: String) {
        selectedTime = time
        binding.btnOp3.text = selectedTime
        toggleDetailVisibility(isVisible = true)
    }

    fun onMinTimeSelected(option: String) {
        binding.btnOp4.text = option
    }

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
