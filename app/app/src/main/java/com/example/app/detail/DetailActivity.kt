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

    // 평일, 토요일, 공휴일 선택
    lateinit var btnOp1: Button
    lateinit var detailInfoLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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

            // 텍스트 교환
            binding.btnDetailStart1.text = arrivalText
            binding.btnDetailArrival1.text = startText
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


        // 평일, 토요일, 공휴일 선택
        detailInfoLayout = findViewById(R.id.detail_info_layout)

        binding.btnOp1.setOnClickListener {
            // 기존 정보 숨기기
            detailInfoLayout.visibility = View.GONE

            // DaySelectionFragment로 화면 전환
            val fragment = BlankFragment1()
            supportFragmentManager.beginTransaction()
                .replace(R.id.detail_fragment1, fragment)
                .addToBackStack(null)
                .commit()
        }







        }


    }
