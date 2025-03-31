package com.example.app.min

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app.MainActivity
import com.example.app.NaverMapActivity
import com.example.app.R
import com.example.app.SubSearchActivity
import com.example.app.databinding.ActivityTest3Binding
import com.example.app.detail.DetailActivity
import com.example.app.dto.CategoryDTO
import com.example.app.jsy.StationDetailActivity
import com.example.app.retrofit.AppServerInterface
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LocationTest3Activity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private val binding by lazy { ActivityTest3Binding.inflate(layoutInflater) }
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val markers = mutableListOf<Marker>()
    private var selectedDistance = 500
    private val subwayMarkers = mutableListOf<Marker>()
    private var showSubwayMarkers = false // 지하철 마커 표시 여부
    // subwayStations를 가변 리스트로 선언 (mutableListOf 사용)


    // 마커와 연결될 InfoWindow 리스트 추가
    private val infoWindows = mutableListOf<InfoWindow>()
    data class SubwayStation( val scode: Int, val line: Int, val name: String, val tel: String, val location: LatLng)


    private val subwayStations =  mutableListOf<SubwayStation>(

//        중복
        SubwayStation(219,2, "서면역", "051-000-0025", LatLng(35.157759003, 129.059317193)),
        SubwayStation(123, 1, "연산", "051-000-0029", LatLng(35.186072353, 129.081513136)),
        SubwayStation(125, 1, "동래", "051-000-0031", LatLng(35.205522, 129.078491)),
        SubwayStation(124, 1, "교대", "051-000-0030", LatLng(35.195569883, 129.079977037)),
        SubwayStation(208, 2, "수영역", "051-678-6208", LatLng(35.166230982, 129.114740559)),
        SubwayStation(227,2, "사상역", "051-678-6227", LatLng(35.162177950, 128.984673157)),
        SubwayStation(233,2, "덕천역", "051-678-6233", LatLng(35.210256746, 129.005254462)),

        // 1호선
        SubwayStation(95, 1, "다대포해수욕장", "051-000-0001", LatLng(35.048226750, 128.965616308)),
        SubwayStation(96, 1, "다대포항", "051-000-0002", LatLng(35.057718471, 128.971312784)),
        SubwayStation(97, 1, "낫개", "051-000-0003", LatLng(35.065265, 128.979873)),
        SubwayStation(98, 1, "신장림", "051-000-0004", LatLng(35.074812569, 128.976662386)),
        SubwayStation(99, 1, "장림", "051-000-0005", LatLng(35.082096329, 128.977358881)),
        SubwayStation(100, 1, "동매", "051-000-0006", LatLng(35.089764080, 128.973986168)),
        SubwayStation(101, 1, "신평", "051-000-0007", LatLng(35.095237595, 128.960595744)),
        SubwayStation(102, 1, "하단", "051-000-0008", LatLng(35.106262885, 128.966759715)),
        SubwayStation(103, 1,"당리", "051-000-0009", LatLng(35.103581633, 128.973702525)),
        SubwayStation(104, 1,"사하", "051-000-0010", LatLng(35.099848946, 128.983196307)),
        SubwayStation(105, 1,"괴정", "051-000-0011", LatLng(35.099957009, 128.992541464)),
        SubwayStation(106, 1,"대티", "051-000-0012", LatLng(35.103185823, 129.000043935)),
        SubwayStation(107, 1,"서대신", "051-000-0013", LatLng(35.110916821, 129.012088051)),
        SubwayStation(108, 1,"동대신", "051-000-0014", LatLng(35.110291290, 129.017731510)),
        SubwayStation(109, 1,"토성", "051-000-0015", LatLng(35.100722409, 129.019798354)),
        SubwayStation(110, 1,"자갈치", "051-000-0016", LatLng(35.097330220, 129.026501986)),
        SubwayStation(111, 1,"남포", "051-000-0017", LatLng(35.097896588, 129.034652071)),
        SubwayStation(112, 1,"중앙", "051-000-0018", LatLng(35.103955, 129.036395)),
        SubwayStation(113, 1,"부산역", "051-000-0019", LatLng(35.114543693, 129.039332261)),
        SubwayStation(114, 1,"초량", "051-000-0020", LatLng(35.121043841, 129.042916372)),
        SubwayStation(115, 1,"부산진", "051-000-0021", LatLng(35.127720909, 129.047755972)),
        SubwayStation(116, 1,"좌천", "051-000-0022", LatLng(35.134323157, 129.054338720)),
        SubwayStation(117, 1,"범일", "051-000-0023", LatLng(35.140789534, 129.059350310)),
        SubwayStation(118, 1,"범내골", "051-000-0024", LatLng(35.147270790, 129.059239350)),
        SubwayStation(120, 1,"부전", "051-000-0026", LatLng(35.162475, 129.062860)),
        SubwayStation(121, 1,"양정", "051-000-0027", LatLng(35.172930480, 129.071179739)),
        SubwayStation(122, 1,"시청", "051-000-0028", LatLng(35.179714247, 129.076554480)),
        SubwayStation(126, 1,"명륜", "051-000-0032", LatLng(35.212574649, 129.079713604)),
        SubwayStation(127, 1, "온천장", "051-000-0033", LatLng(35.220254511, 129.086399582)),
        SubwayStation(128, 1, "부산대", "051-000-0034", LatLng(35.229619804, 129.089396062)),
        SubwayStation(129, 1, "장전", "051-000-0035", LatLng(35.237994756, 129.088161058)),
        SubwayStation(130, 1, "구서", "051-000-0036", LatLng(35.247168706, 129.091274418)),
        SubwayStation(131, 1, "두실", "051-000-0037", LatLng(35.256980463, 129.091372723)),
        SubwayStation(132, 1, "남산", "051-000-0038", LatLng(35.265244417, 129.092387228)),
        SubwayStation(133, 1, "범어사", "051-000-0039", LatLng(35.273125137, 129.092633911)),
        SubwayStation(134, 1, "노포", "051-000-0040", LatLng(35.283966763, 129.095053241)),

//        2호선
        SubwayStation(201, 2,"장산역", "051-678-6201", LatLng(35.170104986, 129.177243972)),
        SubwayStation(202,2,"중동역", "051-678-6202", LatLng(35.166615974, 129.167789210)),
        SubwayStation(203,2,"해운대역", "051-678-6203", LatLng(35.163593025, 129.158720478)),
        SubwayStation(204,2,"동백역", "051-678-6204", LatLng(35.161002844, 129.148573511)),
        SubwayStation(205,2,"벡스코역", "051-678-6205", LatLng(35.168967330, 129.138662244)),
        SubwayStation(206,2,"센텀시티역", "051-678-6206", LatLng(35.169382319, 129.132529492)),
        SubwayStation(207,2,"민락역", "051-678-6207", LatLng(35.167321526, 129.121708487)),
        SubwayStation(209,2,"광안역", "051-678-6209", LatLng(35.157729627, 129.113006079)),
        SubwayStation(210,2,"금련산역", "051-678-6210", LatLng(35.150072081, 129.110917747)),
        SubwayStation(211,2,"남천역", "051-678-6211", LatLng(35.142117945, 129.107841815)),
        SubwayStation(212,2,"경성대·부경대역", "051-678-6212", LatLng(35.137575773, 129.100374262)),
        SubwayStation(213,2,"대연역", "051-678-6213", LatLng(35.135142555, 129.092278467)),
        SubwayStation(214,2,"못골역", "051-678-6214", LatLng(35.134729166, 129.084555262)),
        SubwayStation(215,2,"지게골역", "051-678-6215", LatLng(35.1230, 129.0765)),
        SubwayStation(216,2,"문현역", "051-678-6216", LatLng(35.135607636, 129.074200085)),
        SubwayStation(217,2,"국제금융센터·부산은행역", "051-678-6217", LatLng(35.145554890, 129.066675900)),
        SubwayStation(218,2,"전포역", "051-678-6218", LatLng(35.152854756, 129.065219588)),
        SubwayStation(220,2,"부암역", "051-678-6220", LatLng(35.157462694, 129.050478472)),
        SubwayStation(221,2,"가야역", "051-678-6221", LatLng(35.155807538, 129.042697364)),
        SubwayStation(222,2,"동의대역", "051-678-6222", LatLng(35.154161111, 129.032424472)),
        SubwayStation(223,2,"개금역", "051-678-6223", LatLng(35.153134838, 129.019834462)),
        SubwayStation(224,2,"냉정역", "051-678-6224", LatLng(35.151162307, 129.011915576)),
        SubwayStation(225,2,"주례역", "051-678-6225", LatLng(35.150416815, 129.002975202)),
        SubwayStation(226,2,"감전역", "051-678-6226", LatLng(35.155651889, 128.990911517)),
        SubwayStation(228,2,"덕포역", "051-678-6228", LatLng(35.173929393, 128.983967354)),
        SubwayStation(229,2,"모덕역", "051-678-6229", LatLng(35.181133918, 128.985735241)),
        SubwayStation(230,2,"모라역", "051-678-6230", LatLng(35.189768954, 128.988658040)),
        SubwayStation(231,2,"구남역", "051-678-6231", LatLng(35.197972362, 128.995761871)),
        SubwayStation(232,2,"구명역", "051-678-6232", LatLng(35.202768002, 128.999506166)),
        SubwayStation(234,2,"수정역", "051-678-6234", LatLng(35.222869163, 129.008976278)),
        SubwayStation(235,2,"화명역", "051-678-6235", LatLng(35.234547492, 129.013748318)),
        SubwayStation(236,2,"율리역", "051-678-6236", LatLng(35.246776371, 129.012849437)),
        SubwayStation(237,2,"동원역", "051-678-6237", LatLng(35.259869050, 129.013457105)),
        SubwayStation(238,2,"금곡역", "051-678-6238", LatLng(35.266915486, 129.017042052)),
        SubwayStation(239,2,"호포역", "051-678-6239", LatLng(35.280051325, 129.017760776)),
        SubwayStation(240,2,"증산역", "051-678-6240", LatLng(35.308245238, 129.010457274)),
        SubwayStation(241,2,"부산대양산캠퍼스역", "051-678-6241", LatLng(35.316659574, 129.013939817)),
        SubwayStation(242,2,"남양산역", "051-678-6242", LatLng(35.325410872, 129.019374378)),
        SubwayStation(243,2,"양산역", "051-678-6243", LatLng(35.338602780, 129.026357306)),

    )

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        enableEdgeToEdge()

//        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient("s5lqkqcw4y")

        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient("s06jicv68m")
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }

        // Retrofit 초기화
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.100.203.88:8080/app/")  // 실제 서버 URL로 변경
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(AppServerInterface::class.java)

        // API에서 지하철역 데이터 가져오기
//        fetchSubwayStations(api)

        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheet))
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        // 툴바 가져오기
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        // 기존에 있던 Toolbar 안 내용 모두 제거
        toolbar.removeAllViews()
        toolbar.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white))

//        툴바 안 내용 변경
//        검색
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

// 툴바 안 내용 수정
        val titleEditView = EditText(this).apply {
            hint = "더블클릭 해주세요."
            setHintTextColor(resources.getColor(android.R.color.darker_gray))
            setTextColor(resources.getColor(android.R.color.black))
            textSize = 16f
            background = ContextCompat.getDrawable(
                this@LocationTest3Activity,
                R.drawable.border_box
            )
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
        }

// 스피너 만들기
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
                listOf("300m", "500m", "1km", "2km")
            )
            adapter = spinnerAdapter
        }

        searchContainer.addView(titleEditView)
        searchContainer.addView(searchSpinner)

        toolbar.addView(searchContainer)

        // 툴바 설정
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼

        val chipGroup = findViewById<ChipGroup>(R.id.chip_group)
        val chipSubway = findViewById<Chip>(R.id.chip_subway)

        chipSubway.setOnCheckedChangeListener { _, isChecked ->
            showSubwayMarkers = isChecked
            updateMarkers()
        }


        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val userLocation = LatLng(location.latitude, location.longitude)

                val options = NaverMapOptions().camera(CameraPosition(userLocation, 16.0))
                val mapFragment = MapFragment.newInstance(options)
                supportFragmentManager.beginTransaction().replace(binding.naverMap.id, mapFragment).commit()
                mapFragment.getMapAsync(this)
            }
        }

//        fusedLocationClient.lastLocation.addOnSuccessListener(this, OnSuccessListener { location ->
//
//            if (location != null) {
//
//                // 위치를 가져왔을 때
//                val userLocation = LatLng(location.latitude, location.longitude)
//
//                // NaverMapOptions 설정
//                val options = NaverMapOptions()
//                    .camera(CameraPosition(userLocation, 16.0))
//                    .mapType(NaverMap.MapType.Terrain)
//
//            }
//
//        })
//
//
//
//        // 지도 뷰 가져오기
//        val mapFragment = supportFragmentManager.findFragmentById(R.id.naver_map) as MapFragment?
//            ?: MapFragment.newInstance().also {
//                supportFragmentManager.beginTransaction().add(R.id.naver_map, it).commit()
//            }
//        mapFragment.getMapAsync(this)

        //        시트 숨기기
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        // 스피너 선택 이벤트
        searchSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedDistance = when (position) {
                    0 -> 300
                    1 -> 500
                    2 -> 1000
                    3 -> 2000
                    else -> 500
                }
                if (::naverMap.isInitialized) updateMarkers() // 기존 방식 유지

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // 카테고리 데이터 가져오기
        fetchCategories(api)

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)



//        검색 클릭
        searchContainer.setOnClickListener {
            val intent = Intent(this, SubSearchActivity::class.java)
            startActivity(intent)
        }

        titleEditView.setOnClickListener {
            val intent = Intent(this, SubSearchActivity::class.java)
            startActivity(intent)
        }

//        titleEditView.setOnEditorActionListener { _, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                val searchText = titleEditView.text.toString().trim()
//
////                performSearch(searchText)
//                true
//            } else {
//                false
//            }
//
//        }

        // 검색 클릭
//        val edit: EditText = findViewById(R.id.search)
//        btnStart.setOnClickListener {
//            val stationName = findViewById<TextView>(R.id.station_name).text.toString()
//
//            val intent = Intent(this, MainActivity::class.java).apply {
//                putExtra("selectedStation", stationName)
//            }
//            startActivity(intent)
//            finish() // 현재 액티비티 종료
//        }

//        역 정보 클릭
        val stationinfo : ImageView = findViewById(R.id.station_info_icon)
        stationinfo.setOnClickListener {
            val stationName = findViewById<TextView>(R.id.station_name).text.toString()

            val station = subwayStations.find { it.name == stationName }

            if (station != null) {
                val scode = station.scode
                val lineNumber = station.line

                // 이후 lineNumber를 이용한 처리
                Log.d("Station Info", "Station: $stationName, Line: $lineNumber")

                val intent = Intent(this, StationDetailActivity::class.java).apply {
                    putExtra("stationName", stationName)
                    putExtra("scode", scode)
                    putExtra("line", lineNumber)
                }
                startActivity(intent)
                finish()

            } else {
                Log.e("Station Info", "해당 역을 찾을 수 없습니다: $stationName")
                Toast.makeText(this, "역 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            }


        }

//        출구 정보 클릭
        val exitinfo : ConstraintLayout = findViewById(R.id.exit_container)
        exitinfo.setOnClickListener {
            val stationName = findViewById<TextView>(R.id.station_name).text.toString()

            val station = subwayStations.find { it.name == stationName }

            if (station != null) {
                val scode = station.scode
                val lineNumber = station.line

                // 이후 lineNumber를 이용한 처리
                Log.d("Station Info", "Station: $stationName, Line: $lineNumber")

                val intent = Intent(this, StationDetailActivity::class.java).apply {
                    putExtra("stationName", stationName)
                    putExtra("scode", scode)
                    putExtra("line", lineNumber)
                }
                startActivity(intent)
                finish()

            } else {
                Log.e("Station Info", "해당 역을 찾을 수 없습니다: $stationName")
                Toast.makeText(this, "역 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 출발 버튼 클릭 리스너 추가
        val btnStart: Button = findViewById(R.id.start)
        btnStart.setOnClickListener {
            val selectedDeparture = findViewById<TextView>(R.id.station_name).text.toString()

            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("selectedDeparture", selectedDeparture)
            }
            startActivity(intent)
            finish() // 현재 액티비티 종료
        }

//        도착역 버튼 클릭
        val btnArrive : Button = findViewById(R.id.arrive)
        btnArrive.setOnClickListener {
            val stationArrival = findViewById<TextView>(R.id.station_name).text.toString()

            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("selectedArrival", stationArrival)
            }
            startActivity(intent)
            finish()
        }

        val  timetable : ConstraintLayout = findViewById(R.id.time_table_container)
        timetable.setOnClickListener {
            val stationName = findViewById<TextView>(R.id.station_name).text.toString()

            val station = subwayStations.find { it.name == stationName }

            if (station != null) {
                val scode = station.scode
                val lineNumber = station.line

                // 이후 lineNumber를 이용한 처리
                Log.d("Station Info", "Station: $stationName, Line: $lineNumber")

                val intent = Intent(this, StationDetailActivity::class.java).apply {
                    putExtra("stationName", stationName)
                    putExtra("scode", scode)
                    putExtra("line", lineNumber)
                }
                startActivity(intent)
                finish()

            } else {
                Log.e("Station Info", "해당 역을 찾을 수 없습니다: $stationName")
                Toast.makeText(this, "역 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun performSearch(query: String) {
        if (query.isNotBlank()) {
            Toast.makeText(this, "검색: $query", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchCategories(api: AppServerInterface) {
        api.getCategories().enqueue(object : Callback<List<CategoryDTO>> {
            override fun onResponse(call: Call<List<CategoryDTO>>, response: Response<List<CategoryDTO>>) {
                if (response.isSuccessful && response.body() != null) {
                    val categories = response.body()

                    // Category 이름을 가져와서 SubwayStation에 적용
                    categories?.forEach { category ->
                        subwayStations.forEachIndexed { index, station ->
                            // 기존 subwayStations의 정보에서 이름만 category의 name으로 업데이트
                            if (station.name == station.name) {
                                subwayStations[index] = SubwayStation(
                                    station.scode,
                                    station.line,
                                    station.name,
//                                    category.name,  // 새로운 카테고리 이름으로 업데이트
                                    station.tel,     // 기존 전화번호 유지
                                    station.location // 기존 좌표 유지
                                )
                            }
                        }
                    }

                    Log.d("API Response", "Categories fetched successfully")
                    updateMarkers()
                } else {
                    Log.e("API Error", "Error: ${response.code()} - ${response.message()}")
                    Toast.makeText(this@LocationTest3Activity, "카테고리 데이터를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<CategoryDTO>>, t: Throwable) {
                Log.e("API Failure", "Failed to fetch categories", t)
                Toast.makeText(this@LocationTest3Activity, "API 호출 실패: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    //    뒤로 가기
override fun onSupportNavigateUp(): Boolean {
    finish()
    return true
}

// MapReady
    override fun onMapReady(map: NaverMap) {
        naverMap = map
        naverMap.locationSource = locationSource

        naverMap.setOnMapClickListener { _, _ ->
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        // 기본 UI 설정 활성화
        val uiSettings = naverMap.uiSettings
        uiSettings.isLocationButtonEnabled = true // 기본 위치 버튼 숨김

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            naverMap.locationTrackingMode = LocationTrackingMode.NoFollow // 자동 추적 모드 해제
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    updateLocationOverlay(location)
                    updateMarkers()
                }
            }
        } else {
            ActivityCompat.requestPermissions(this, LOCATION_PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

//    위치 오버레이
    private fun updateLocationOverlay(location: Location) {
        val userLocation = LatLng(location.latitude, location.longitude)

        val locationOverlay = naverMap.locationOverlay
        locationOverlay.isVisible = true // 항상 보이도록 설정
        locationOverlay.icon = OverlayImage.fromResource(R.drawable.location_overlay_icon)
        locationOverlay.position = userLocation
        locationOverlay.iconWidth = 80
        locationOverlay.iconHeight = 80
    }

// 마커 생성 및 거리 계산
    private fun updateMarkers() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val userLocation = LatLng(location.latitude, location.longitude)

                markers.forEach { it.map = null }
                infoWindows.forEach { it.map = null }
                markers.clear()
                infoWindows.clear()

//
                if (showSubwayMarkers) {
                    val filteredStations = subwayStations.filter { station ->
                        val results = FloatArray(1)
                        Location.distanceBetween(
                            userLocation.latitude, userLocation.longitude,
                            station.location.latitude, station.location.longitude,
                            results
                        )
                        results[0] <= selectedDistance
                    }

                    filteredStations.forEach { station ->
                        val results = FloatArray(1)
                        Location.distanceBetween(
                            userLocation.latitude, userLocation.longitude,
                            station.location.latitude, station.location.longitude,
                            results
                        )
                        val distance = results[0]

                        val marker = Marker().apply {
                            position = station.location
                            map = naverMap
                        }

                        // InfoWindow 생성 및 적용
                        val infoWindow = InfoWindow().apply {
                            adapter = object : InfoWindow.DefaultTextAdapter(this@LocationTest3Activity) {
                                override fun getText(infoWindow: InfoWindow): CharSequence {
                                    return "${station.name} (${if (distance < 1000) "${distance.toInt()}m" else String.format("%.1fkm", distance / 1000)})"
                                }
                            }
                            map = null // 기본적으로 숨김 처리
                        }

                        // **Chip이 활성화된 경우 InfoWindow 자동 표시**
                        if (showSubwayMarkers) {
                            infoWindow.open(marker)
                        }

                        marker.setOnClickListener {
                            if (infoWindow.map == null) {
                                infoWindow.open(marker)
                            }

                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                            findViewById<TextView>(R.id.station_name).text = station.name
                            findViewById<TextView>(R.id.tel).text = station.tel
                            findViewById<TextView>(R.id.distance).text = if (distance < 1000) {
                                "${distance.toInt()}m"
                            } else {
                                String.format("%.1fkm", distance / 1000)
                            }
                            true
                        }
                        markers.add(marker)
                        infoWindows.add(infoWindow)
                    }
                }
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        private val LOCATION_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}


