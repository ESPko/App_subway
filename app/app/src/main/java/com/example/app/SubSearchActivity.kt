package com.example.app

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

//  역 검색 화면을 관리하는 액티비티
class SubSearchActivity : AppCompatActivity() {


  private val binding:ActivitySubSearchBinding by lazy {
    ActivitySubSearchBinding.inflate(layoutInflater)
  }

//  SubSearchAdapter 연결
  private lateinit var stationAdapter: SubSearchAdapter

  // 전체 역 리스트 저장
  private var allStations: List<Station> = emptyList()

// Retrofit 인스턴스 가져오기
  private val apiService: AppServerInterface = AppServerClass.instance


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContentView(binding.root)

    ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
      insets
    }
// RecyclerView 설정 함수 호출
    setupRecyclerView()
//    노선 필터 버튼
    setupFilterButtons()
//    검색
    setupSearch()


//  테스트용 더미
    loadDummyStations()

    stationAdapter.updateData(allStations)


////    서버에서 역 목록
//    loadStationsFromServer()

  }

//  더미 데이터
  private fun loadDummyStations() {
    allStations = listOf(
      // 1호선
      Station("다대포해수욕장", 1),
      Station("다대포항", 1),
      Station("낫개", 1),
      Station("하단", 1),
      Station("서면", 1),
      Station("부산역", 1),

      // 2호선
      Station("장산", 2),
      Station("해운대", 2),
      Station("동백", 2),
      Station("수영", 2),
      Station("광안", 2),
      Station("서면", 2),

      // 3호선
      Station("대저", 3),
      Station("체육공원", 3),
      Station("강서구청", 3),
      Station("연산", 3),
      Station("수영", 3),
      Station("미남", 3),

      // 4호선
      Station("안평", 4),
      Station("석대", 4),
      Station("반여", 4),
      Station("동래", 4),
      Station("미남", 4)
    )
  }



  // 역 목록 데이터 생성
  private fun setupRecyclerView(){
    stationAdapter = SubSearchAdapter(emptyList())
    binding.rvStation.apply{
      adapter = stationAdapter
      layoutManager = LinearLayoutManager(this@SubSearchActivity)
    }
  }

//  호선 버튼
  private fun setupFilterButtons(){
    binding.btnLine1.setOnClickListener { filterStations(1) }
    binding.btnLine2.setOnClickListener { filterStations(2) }
    binding.btnLine3.setOnClickListener { filterStations(3) }
    binding.btnLine4.setOnClickListener { filterStations(4) }
  }


  private fun searchStations(query: String) {
    val filteredStations = allStations.filter { it.name.contains(query, ignoreCase = true) }
    stationAdapter.updateData(filteredStations)
  }

  private fun setupSearch() {
    binding.searchBar.addTextChangedListener(object : TextWatcher {
      override fun afterTextChanged(s: Editable?) {
        val query = s?.toString()?.trim() ?: ""
        searchStations(query)
      }

      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
  }


//  서버에서 역정보 들고오기
  private fun loadStationsFromServer() {
    apiService.getStations().enqueue(object : Callback<List<Station>> {
      override fun onResponse(call: Call<List<Station>>, response: Response<List<Station>>) {
        if (response.isSuccessful) {
          response.body()?.let { stations ->
            allStations = stations
            stationAdapter.updateData(stations) // RecyclerView 업데이트
          }
        }
      }

      override fun onFailure(call: Call<List<Station>>, t: Throwable) {
        Log.d("fullstack503", "서버에서 데이터 가져오기 실패: ${t.message}")
      }
    })
  }

  // 노선에 따라 필터링하는 함수
  private fun filterStations(line: Int) {
    allStations?.let { stations ->  }
    val filteredStations = allStations.filter { it.line == line }
    stationAdapter.updateData(filteredStations)
  }
}