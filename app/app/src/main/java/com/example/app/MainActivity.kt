package com.example.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.ScaleGestureDetector
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app.databinding.ActivityMainBinding
import com.example.app.detail.DetailActivity
import com.example.app.dto.CategoryDTO
import com.example.app.min.LocationTest3Activity
import com.example.app.min.QuickTest2Activity
import com.example.app.min.SettingTest4Activity
import com.example.app.retrofit.AppServerInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {

  private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
  private lateinit var categories: List<CategoryDTO> // 카테고리 데이터를 저장할 변수 추가
  private var selectedDeparture: CategoryDTO? = null // 선택된 출발역
  private var selectedArrival: CategoryDTO? = null // 선택된 도착역

  private lateinit var scaleGestureDetector: ScaleGestureDetector
  private var scaleFactor = 1f // 초기 크기 (배율)



  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)

    // ScaleGestureDetector 초기화
    scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())



    // Intent에서 출발역과 도착역 값 가져오기
    val receivedDeparture = intent.getStringExtra("selectedDeparture")
    val receivedArrival = intent.getStringExtra("selectedArrival")

    // 값이 있을 경우 버튼 텍스트 및 변수 업데이트
    if (receivedDeparture != null) {
      selectedDeparture = CategoryDTO(receivedDeparture, 0, 0) // 기본값 0으로 설정
      binding.btnDeparture.text = receivedDeparture
    }

    if (receivedArrival != null) {
      selectedArrival = CategoryDTO(receivedArrival, 0, 0) // 기본값 0으로 설정
      binding.btnArrival.text = receivedArrival
    }

      val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)

      toolbar.findViewById<LinearLayout>(R.id.search).setOnClickListener {
          Toast.makeText(this, "역검색 클릭", Toast.LENGTH_SHORT).show()
          startActivity(Intent(this, SubSearchActivity::class.java))
      }

//      toolbar.findViewById<LinearLayout>(R.id.quick_search).setOnClickListener {
//          Toast.makeText(this, "빠른검색 클릭", Toast.LENGTH_SHORT).show()
//          startActivity(Intent(this, QuickTest2Activity::class.java))
//      }

      toolbar.findViewById<LinearLayout>(R.id.around).setOnClickListener {
          Toast.makeText(this, "내 주변 클릭", Toast.LENGTH_SHORT).show()
          startActivity(Intent(this, LocationTest3Activity::class.java))
      }

//      toolbar.findViewById<LinearLayout>(R.id.setting).setOnClickListener {
//          Toast.makeText(this, "설정 클릭", Toast.LENGTH_SHORT).show()
//          startActivity(Intent(this, SettingTest4Activity::class.java))
//      }
//
//      toolbar.findViewById<LinearLayout>(R.id.more).setOnClickListener {
//          Toast.makeText(this, "더보기 클릭", Toast.LENGTH_SHORT).show()
//      }

    // 검색 버튼 클릭 리스너
//    binding.btnSearch.setOnClickListener {
//      val intent = Intent(this@MainActivity, SubSearchActivity::class.java)
//      startActivity(intent)
//      finish()
//    }

    // Retrofit 초기화
    val retrofit = Retrofit.Builder()
      .baseUrl("http://10.100.203.88:8080/app/")  // 실제 서버 URL로 변경
      .addConverterFactory(GsonConverterFactory.create())
      .build()

    val api = retrofit.create(AppServerInterface::class.java)

    val btnDeparture: Button = findViewById(R.id.btnDeparture)
    val btnArrival: Button = findViewById(R.id.btnArrival)

    // 출발역 버튼 클릭 리스너
    btnDeparture.setOnClickListener {
      // 출발역 데이터 가져오기
      fetchCategories(api, "departure")
    }

    // 도착역 버튼 클릭 리스너
    btnArrival.setOnClickListener {
      // 도착역 데이터 가져오기
      fetchCategories(api, "arrival")
    }

    // 'Intro' 버튼 클릭 시 출발역과 도착역이 선택되지 않았으면 알림
    binding.btnIntro.setOnClickListener {
      if (selectedDeparture == null || selectedArrival == null) {
        Toast.makeText(this@MainActivity, "출발역과 도착역을 선택해 주세요.", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
      }

      // 선택된 출발역과 도착역을 포함한 CategoryDTO 리스트 생성
      val categoryList = categories.toMutableList() // 이미 가져온 카테고리 데이터
      val intent = Intent(this@MainActivity, DetailActivity::class.java).apply {
        // Pass the selected departure and arrival as extras in the Intent
        putExtra("departure", selectedDeparture)
        putExtra("arrival", selectedArrival)
      }
      Log.d("csy", "출발역: ${selectedDeparture?.name}, 도착역: ${selectedArrival?.name}")
      startActivity(intent)
      finish()
    }

    // ImageView 확대/축소 기능 추가
    binding.mainRoute.setOnTouchListener { _, event ->
      scaleGestureDetector.onTouchEvent(event)
      true
    }

  }

  // Retrofit을 통해 카테고리 데이터를 가져오는 함수
  private fun fetchCategories(api: AppServerInterface, type: String) {
    api.getCategories().enqueue(object : Callback<List<CategoryDTO>> {
      override fun onResponse(call: Call<List<CategoryDTO>>, response: Response<List<CategoryDTO>>) {
        if (response.isSuccessful && response.body() != null) {
          val categories = response.body()
          this@MainActivity.categories = categories ?: emptyList() // 가져온 카테고리 데이터를 클래스 변수에 저장

          val categoryNames = categories?.map { it.name } ?: emptyList()

          // PopupMenu로 리스트 표시
          showPopupMenu(categoryNames, type)
          Log.d("API Response", "Categories fetched successfully: $categoryNames")
        } else {
          // 서버 응답이 성공적이지 않음
          Log.e("API Error", "Error: ${response.code()} - ${response.message()}")
          Toast.makeText(this@MainActivity, "카테고리 데이터를 가져오는데 실패했습니다. (응답 코드: ${response.code()})", Toast.LENGTH_SHORT).show()
        }
      }

      override fun onFailure(call: Call<List<CategoryDTO>>, t: Throwable) {
        // API 호출 실패 시
        Log.e("API Failure", "Failed to fetch categories", t)
        Toast.makeText(this@MainActivity, "API 호출 실패: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
      }
    })
  }

  // PopupMenu로 카테고리 리스트를 표시하고, 선택된 항목을 버튼 텍스트에 설정
  private fun showPopupMenu(categoryNames: List<String>, type: String) {
    val button: Button = if (type == "departure") {
      findViewById(R.id.btnDeparture)
    } else {
      findViewById(R.id.btnArrival)
    }

    val popupMenu = PopupMenu(this, button)
    categoryNames.forEach { name ->
      popupMenu.menu.add(name)
    }

    popupMenu.setOnMenuItemClickListener { item: MenuItem ->
      button.text = item.title  // 선택된 항목을 버튼 텍스트로 설정

      // 선택된 카테고리 찾기
      val selectedCategory = categories.find { it.name == item.title.toString() }

      if (type == "departure") {
        selectedDeparture = selectedCategory  // 출발역 선택 저장
      } else {
        selectedArrival = selectedCategory  // 도착역 선택 저장
      }

      true
    }

    popupMenu.show()
  }

  // ScaleListener로 노선도 확대/축소 설정
  private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener(){
    override fun onScale(detector: ScaleGestureDetector): Boolean {
      // 두 손가락의 확대/축소 배율을 받아옵니다.
      scaleFactor *= detector.scaleFactor
      scaleFactor = scaleFactor.coerceIn(0.1f, 5f) // 최소 0.1배, 최대 5배까지 확대 가능

      // 이미지를 줌(확대/축소)합니다.
      binding.mainRoute.scaleX = scaleFactor
      binding.mainRoute.scaleY = scaleFactor

      return true
    }
  }
}
