package com.example.app.detail

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app.R
import com.example.app.databinding.ActivityGetOffBinding

class GetOffActivity : AppCompatActivity() {

    private val binding: ActivityGetOffBinding by lazy {
        ActivityGetOffBinding.inflate(layoutInflater)
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
        // 닫기 버튼 클릭 시 DetailActivity로 돌아가기
        binding.closeBtn.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            startActivity(intent)
            finish()
        }
        // btn_detail_start2 버튼 클릭 시 StartBtnFragment를 다이얼로그로 표시
        binding.btnDetailStart2.setOnClickListener {
            val startBtnFragment = StartBtnFragment()
            startBtnFragment.show(supportFragmentManager, "startBtnFragment")
        }
        // btn_detail_arrival2 버튼 클릭 시 ArrivalBtnFragment를 다이얼로그로 표시
        binding.btnDetailArrival2.setOnClickListener {
            val arrivalBtnFragment = ArrivalBtnFragment()
            arrivalBtnFragment.show(supportFragmentManager, "arrivalBtnFragment")
        }
    }
}