package com.example.app.min

import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app.R
import com.example.app.databinding.ActivityTest2Binding

class QuickTest2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var binding = ActivityTest2Binding.inflate(layoutInflater)
        setContentView(binding.root)


        // 툴바 가져오기
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        // 기존에 있던 Toolbar 안 내용 모두 제거
        toolbar.removeAllViews()

        // 변경될 툴바 내용
        val titleTextView = TextView(this).apply {
            text = "빠른 검색"
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

//        편집
        val editButton = TextView(this).apply {
            text = "편집"
            setTextColor(resources.getColor(android.R.color.white))
            textSize = 16f
            setPadding(16, 0, 16, 0)
            layoutParams = Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.END or Gravity.CENTER_VERTICAL
            }

            // 클릭 이벤트
            setOnClickListener {
                Toast.makeText(this@QuickTest2Activity, "편집 클릭됨", Toast.LENGTH_SHORT).show()
            }
        }


        toolbar.addView(editButton)

        // 툴바 설정
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼

        toolbar.navigationIcon?.setTint(ContextCompat.getColor(this, android.R.color.white))

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
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



