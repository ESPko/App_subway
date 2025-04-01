package com.example.app

import SubSearchAdapter
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app.databinding.ActivitySubSearchBinding
import com.example.app.retrofit.AppServerClass
import com.example.app.retrofit.AppServerInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.app.dto.CategoryDTO

// 역 검색 화면을 관리하는 액티비티
class SubSearchActivity : AppCompatActivity() {

  private val binding: ActivitySubSearchBinding by lazy {
    ActivitySubSearchBinding.inflate(layoutInflater)
  }

  // SubSearchAdapter 연결
  private lateinit var stationAdapter: SubSearchAdapter

  // 전체 역 리스트 저장
  private var allStationSearch: List<CategoryDTO> = emptyList()

  // 현재 선택된 필터 상태
  private var selectedLine: Int? = null  // 수정된 부분
  private var searchQuery: String = ""

  // Retrofit 인스턴스 가져오기
  private val apiService: AppServerInterface = AppServerClass.instance

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
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

    ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
      insets
    }

    // RecyclerView 설정 함수 호출
    setupRecyclerView()

    // 노선 필터 버튼
    setupFilterButtons()

    // 검색
    setupSearch()

    // 서버에서 역 목록
    loadStationsFromServer()
  }

  // 역 목록 데이터 생성
  private fun setupRecyclerView() {
    stationAdapter = SubSearchAdapter(allStationSearch)
    binding.rvStation.apply {
      adapter = stationAdapter
      layoutManager = LinearLayoutManager(this@SubSearchActivity)
    }
  }

  // 노선별 색상 지정
  private val lineColors = mapOf(
    1 to "#F06A00", // 1호선 주황색
    2 to "#81BF48", // 2호선 초록색
    3 to "#BB8C00", // 3호선 갈색
    4 to "#217DCB"  // 4호선 파랑색
  )

  // 비활성화 버튼 색상
  private val defaultTextColor = "#BDBDBD"

  // 호선 버튼 클릭 설정
  private fun setupFilterButtons() {
    val buttons = mapOf(
      1 to binding.btnLine1,
      2 to binding.btnLine2,
      3 to binding.btnLine3,
      4 to binding.btnLine4
    )
    buttons.forEach { (line, button) ->
      button.setOnClickListener {
        selectedLine = if (selectedLine == line) null else line  // selectedLine 변경
        updateButtonStyles(buttons)
        applyFilters()
      }
    }
  }

  // 버튼 스타일 클릭 시 반응
  private fun updateButtonStyles(buttons: Map<Int, TextView>) {
    buttons.forEach { (line, button) ->
      if (selectedLine == line) {
        button.setTextColor(Color.parseColor(lineColors[line]))
        button.setTypeface(null, Typeface.BOLD)
      } else {
        button.setTextColor(Color.parseColor(defaultTextColor))
        button.setTypeface(null, Typeface.NORMAL)
      }
    }
  }

  // 역 검색 기능
  private fun setupSearch() {
    binding.searchBar.addTextChangedListener(object : TextWatcher {
      override fun afterTextChanged(s: Editable?) {
        searchQuery = s?.toString()?.trim() ?: ""
        applyFilters()
      }

      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
  }

  // 필터 적용 (검색어 + 노선)
  private fun applyFilters() {
    var filteredStations = allStationSearch

    // 검색어 필터
    if (searchQuery.isNotEmpty()) {
      filteredStations = filteredStations.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    // 노선 필터
    selectedLine?.let { line ->
      filteredStations = filteredStations.filter { it.line == line }
    }

    stationAdapter.updateData(filteredStations)
  }

  // 서버에서 역정보 들고오기
  private fun loadStationsFromServer() {
    apiService.getCategories().enqueue(object : Callback<List<CategoryDTO>> {
      override fun onResponse(call: Call<List<CategoryDTO>>, response: Response<List<CategoryDTO>>) {
        if (response.isSuccessful) {
          response.body()?.let { stations ->
            allStationSearch = stations
            stationAdapter.updateData(stations)
          }
        } else {
          Log.d("fullstack503", "서버 응답 실패: ${response.errorBody()?.string()}")
        }
      }

      override fun onFailure(call: Call<List<CategoryDTO>>, t: Throwable) {
        Log.d("fullstack503", "서버에서 데이터 가져오기 실패: ${t.message}")
      }
    })
  }

      override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
