//package com.example.app.min
//
//import android.os.Bundle
//import android.view.Gravity
//import android.widget.TextView
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.appcompat.widget.Toolbar
//import androidx.core.content.ContextCompat
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import com.example.app.R
//import com.example.app.databinding.ActivityTestBinding
//import android.text.Editable
//import android.text.TextWatcher
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.app.retrofit.AppServerClass
//import com.example.app.retrofit.AppServerInterface
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import android.util.Log
//import com.example.app.Station
//
//
//class SearchTestActivity : AppCompatActivity() {
//
//    private val binding:ActivityTestBinding by lazy {
//        ActivityTestBinding.inflate(layoutInflater)
//    }
//
//    //  SubSearchAdapter 연결
//    private lateinit var stationAdapter: SubSearchAdapterTest
//
//    // 전체 역 리스트 저장
//    private var allStations: List<Station> = emptyList()
//
//    // Retrofit 인스턴스 가져오기
//    private val apiService: AppServerInterface = AppServerClass.instance
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//
//        var binding = ActivityTestBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // 툴바 가져오기
//        val toolbar = findViewById<Toolbar>(R.id.toolbar)
//
//        // 기존에 있던 Toolbar 안 내용 모두 제거
//        toolbar.removeAllViews()
//
//        // 변경될 툴바 내용
//        val titleTextView = TextView(this).apply {
//            text = "역 검색"
//            setTextColor(resources.getColor(android.R.color.white))
//            textSize = 18f
//            layoutParams = Toolbar.LayoutParams(
//                Toolbar.LayoutParams.WRAP_CONTENT,
//                Toolbar.LayoutParams.WRAP_CONTENT
//            ).apply {
//                gravity = Gravity.CENTER
//            }
//        }
//
//        toolbar.addView(titleTextView)
//
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼
//        supportActionBar?.setDisplayShowTitleEnabled(false) // 기본 타이틀 제거
//
//        toolbar.navigationIcon?.setTint(ContextCompat.getColor(this, android.R.color.white))
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//
//        // RecyclerView 설정 함수 호출
//        setupRecyclerView()
////    노선 필터 버튼
//        setupFilterButtons()
////    검색
//        setupSearch()
//
//        //  테스트용 더미
//        loadDummyStations()
//
//        stationAdapter.updateData(allStations)
//
//
//////    서버에서 역 목록
////    loadStationsFromServer()
//
//    }
//
//    //  더미 데이터
//    private fun loadDummyStations() {
//        allStations = listOf(
//            // 1호선
//            Station("다대포해수욕장", 1),
//            Station("다대포항", 1),
//            Station("낫개", 1),
//            Station("하단", 1),
//            Station("서면", 1),
//            Station("부산역", 1),
//            Station("서면",1),
//
//            // 2호선
//            Station("장산", 2),
//            Station("해운대", 2),
//            Station("동백", 2),
//            Station("수영", 2),
//            Station("광안", 2),
//            Station("서면", 2),
//
//            // 3호선
//            Station("대저", 3),
//            Station("체육공원", 3),
//            Station("강서구청", 3),
//            Station("연산", 3),
//            Station("수영", 3),
//            Station("미남", 3),
//
//            // 4호선
//            Station("안평", 4),
//            Station("석대", 4),
//            Station("반여", 4),
//            Station("동래", 4),
//            Station("미남", 4)
//        )
//    }
//
//
//
//    // 역 목록 데이터 생성
//    private fun setupRecyclerView(){
//        stationAdapter = SubSearchAdapterTest(emptyList())
//        binding.rvStation.apply{
//            adapter = stationAdapter
//            layoutManager = LinearLayoutManager(this@SearchTestActivity)
//        }
//    }
//
//    //  호선 버튼
//    private fun setupFilterButtons(){
//        binding.btnLine1.setOnClickListener { filterStations(1) }
//        binding.btnLine2.setOnClickListener { filterStations(2) }
//        binding.btnLine3.setOnClickListener { filterStations(3) }
//        binding.btnLine4.setOnClickListener { filterStations(4) }
//    }
//
//
//    private fun searchStations(query: String) {
//        val filteredStations = allStations.filter { it.name.contains(query, ignoreCase = true) }
//        stationAdapter.updateData(filteredStations)
//    }
//
//    private fun setupSearch() {
//        binding.searchBar.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                val query = s?.toString()?.trim() ?: ""
//                searchStations(query)
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//        })
//    }
//
//
//    //  서버에서 역정보 들고오기
//    private fun loadStationsFromServer() {
//        apiService.getStations().enqueue(object : Callback<List<Station>> {
//            override fun onResponse(call: Call<List<Station>>, response: Response<List<Station>>) {
//                if (response.isSuccessful) {
//                    response.body()?.let { stations ->
//                        allStations = stations
//                        stationAdapter.updateData(stations) // RecyclerView 업데이트
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<List<Station>>, t: Throwable) {
//                Log.d("fullstack503", "서버에서 데이터 가져오기 실패: ${t.message}")
//            }
//        })
//    }
//
//    // 노선에 따라 필터링하는 함수
//    private fun filterStations(line: Int) {
//        allStations?.let { stations ->  }
//        val filteredStations = allStations.filter { it.line == line }
//        stationAdapter.updateData(filteredStations)
//    }
//
//    override fun onSupportNavigateUp(): Boolean {
//        finish()
//        return true
//    }
//}