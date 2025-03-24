package com.example.app.min

import android.os.Bundle
import android.view.Gravity
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app.databinding.ActivityTest3Binding
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.ArrayAdapter
import com.example.app.R


class LocationTest3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var binding = ActivityTest3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // 툴바 가져오기
        val toolbar = findViewById<Toolbar>(R.id.toolbar)


        // 기존에 있던 Toolbar 안 내용 모두 제거
        toolbar.removeAllViews()
        toolbar.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white))

        val searchContainer = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL

            layoutParams = Toolbar.LayoutParams(
                Toolbar.LayoutParams.MATCH_PARENT,
                Toolbar.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER_VERTICAL
                marginEnd = 80
                marginStart = 80
            }
        }

        val titleEditView = EditText(this).apply {
            hint = "검색어를 입력해 주세요"
            setHintTextColor(resources.getColor(android.R.color.darker_gray))
            setTextColor(resources.getColor(android.R.color.black))
            textSize = 16f
            background = ContextCompat.getDrawable(this@LocationTest3Activity,
                R.drawable.border_box
            )
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f  // 가중치를 1로 설정하여 EditText가 남은 공간을 차지하도록 함
            )
        }

        val searchSpinner = Spinner(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                marginStart = 16
            }

            val spinnerAdapter = ArrayAdapter(
                this@LocationTest3Activity,
                android.R.layout.simple_spinner_dropdown_item,
                listOf("옵션1", "옵션2", "옵션3")
            )
            adapter = spinnerAdapter
        }

        searchContainer.addView(titleEditView)
        searchContainer.addView(searchSpinner)

        toolbar.addView(searchContainer)

        // 툴바 설정
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼

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