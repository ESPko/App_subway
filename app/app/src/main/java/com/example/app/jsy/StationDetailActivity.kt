package com.example.app.jsy

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.app.R
import com.example.app.databinding.ActivityStationDetailBinding

class StationDetailActivity : AppCompatActivity() {

  private val binding:ActivityStationDetailBinding by lazy{
    ActivityStationDetailBinding.inflate(layoutInflater)
  }
  private fun setActiveButton(activeButtonId: Int, inactiveButtonId: Int) {
    when (activeButtonId) {
      R.id.btn_line_1 -> binding.btnLine1.setBackgroundResource(R.drawable.btn_line_1)  // 1호선 배경
      R.id.btn_line_2 -> binding.btnLine2.setBackgroundResource(R.drawable.btn_line_2)  // 2호선 배경
    }

    // 비활성화된 버튼 배경을 테두리만 있는 형태로 변경
    when (inactiveButtonId) {
      R.id.btn_line_1 -> binding.btnLine1.setBackgroundResource(R.drawable.btn_line_1_inactive)
      R.id.btn_line_2 -> binding.btnLine2.setBackgroundResource(R.drawable.btn_line_2_inactive)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContentView(binding.root)
    ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
      insets
    }

    // btn_line_1 버튼 클릭 시 1호선 정보 Fragment 표시
    binding.btnLine1.setOnClickListener {
      setActiveButton(R.id.btn_line_1, R.id.btn_line_2)
      replaceFragment(Line1Fragment())  // 1호선 정보 Fragment로 변경
    }

    // btn_line_2 버튼 클릭 시 2호선 정보 Fragment 표시
    binding.btnLine2.setOnClickListener {
      setActiveButton(R.id.btn_line_2, R.id.btn_line_1)
      replaceFragment(Line2Fragment())  // 2호선 정보 Fragment로 변경
    }
    if (savedInstanceState == null) {
      setActiveButton(R.id.btn_line_1, R.id.btn_line_2) // 초기 상태 설정 (1호선 활성화)
      replaceFragment(Line1Fragment())  // 첫 번째로 1호선 정보 Fragment를 표시
    }
  }
  private fun replaceFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction()
      .replace(R.id.fragment_container, fragment)
      .addToBackStack(null)  // 백스택에 넣어두어 뒤로가기 시 이전 Fragment로 돌아갈 수 있게 함
      .commit()
  }
}