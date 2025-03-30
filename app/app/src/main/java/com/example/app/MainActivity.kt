package com.example.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
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
import com.example.app.min.SearchTestActivity
import com.example.app.min.SettingTest4Activity
import com.example.app.retrofit.AppServerInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

  private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)

    // 인텐트에서 값 가져오기
    val selectedStation = intent.getStringExtra("selectedStation")
    val selectedArrival = intent.getStringExtra("selectedArrival")

    // 값이 존재하면 출발 버튼 텍스트 변경
    selectedStation?.let {
      findViewById<Button>(R.id.btnDeparture).text = it
    }

    selectedArrival?.let {
      findViewById<Button>(R.id.btnArrival).text = it
    }


    val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)

    toolbar.findViewById<LinearLayout>(R.id.search).setOnClickListener {
      Toast.makeText(this, "역검색 클릭", Toast.LENGTH_SHORT).show()
      startActivity(Intent(this, SearchTestActivity::class.java))
    }

    toolbar.findViewById<LinearLayout>(R.id.quick_search).setOnClickListener {
      Toast.makeText(this, "빠른검색 클릭", Toast.LENGTH_SHORT).show()
      startActivity(Intent(this, QuickTest2Activity::class.java))
    }

    toolbar.findViewById<LinearLayout>(R.id.around).setOnClickListener {
      Toast.makeText(this, "내 주변 클릭", Toast.LENGTH_SHORT).show()
      startActivity(Intent(this, LocationTest3Activity::class.java))
    }

    toolbar.findViewById<LinearLayout>(R.id.setting).setOnClickListener {
      Toast.makeText(this, "설정 클릭", Toast.LENGTH_SHORT).show()
      startActivity(Intent(this, SettingTest4Activity::class.java))
    }

    toolbar.findViewById<LinearLayout>(R.id.more).setOnClickListener {
      Toast.makeText(this, "더보기 클릭", Toast.LENGTH_SHORT).show()
    }


    binding.btnSearch.setOnClickListener {
      val intent = Intent(this@MainActivity, SubSearchActivity::class.java)
      startActivity(intent)
      finish()
    }

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

    // 검색 버튼 클릭 리스너
    val searchButton: Button = findViewById(R.id.btn_intro)
    searchButton.setOnClickListener {
      val departureText = btnDeparture.text.toString()
      val arrivalText = btnArrival.text.toString()

      if (departureText.isNotEmpty() && arrivalText.isNotEmpty()) {
        // 검색 후 디테일 액티비티로 이동
        val intent = Intent(this, DetailActivity::class.java).apply {
          putExtra("departure", departureText)
          putExtra("arrival", arrivalText)
        }
        startActivity(intent)
      } else {
        Toast.makeText(this, "출발역과 도착역을 선택해주세요.", Toast.LENGTH_SHORT).show()
      }
    }
  }

  // Retrofit을 통해 카테고리 데이터를 가져오는 함수
  private fun fetchCategories(api: AppServerInterface, type: String) {
    api.getCategories().enqueue(object : Callback<List<CategoryDTO>> {
      override fun onResponse(call: Call<List<CategoryDTO>>, response: Response<List<CategoryDTO>>) {
        if (response.isSuccessful && response.body() != null) {
          val categories = response.body()
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
      true
    }

    popupMenu.show()
  }
}
