package com.example.app

import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app.databinding.ActivityTestBinding

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        var binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 툴바 가져오기
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        // 기존에 있던 Toolbar 안 내용 모두 제거
        toolbar.removeAllViews()

        // 변경될 툴바 내용
        val titleTextView = TextView(this).apply {
            text = "역 검색"
            setTextColor(resources.getColor(android.R.color.white))
            textSize = 18f
            layoutParams = Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER
            }
        }

        toolbar.addView(titleTextView)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼
        supportActionBar?.setDisplayShowTitleEnabled(false) // 기본 타이틀 제거

        toolbar.navigationIcon?.setTint(ContextCompat.getColor(this, android.R.color.white))

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}