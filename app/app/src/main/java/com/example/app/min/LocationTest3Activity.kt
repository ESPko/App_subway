package com.example.app.min

import android.Manifest
import android.content.pm.PackageManager
import android.health.connect.datatypes.ExerciseRoute
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app.R
import com.example.app.databinding.ActivityTest3Binding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource


class LocationTest3Activity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private val binding by lazy { ActivityTest3Binding.inflate(layoutInflater) }

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // 1km 반경 내 역 목록 (임시 데이터, 실제 API 사용 가능)
    private val subwayStations = listOf(
        LatLng(35.164632338, 129.060161295), // 부전역 동해선
        LatLng(35.162474865, 129.062859882), // 부전역 1호선
        LatLng(35.157759003, 129.059317193), // 서면역
        LatLng(35.152854756, 129.065219588), // 전포역
        LatLng(35.147270790, 129.059239350), // 범내골역
        LatLng(35.205521616, 129.078490642)  // 동래역 (1호선, 1km 밖일 수도 있음)
    )


    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var binding = ActivityTest3Binding.inflate(layoutInflater)
        setContentView(binding.root)

//        시트
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheet))

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

//
        val titleEditView = EditText(this).apply {
            hint = "검색어를 입력해 주세요"
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

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // FusedLocationProviderClient 초기화
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // 위치를 가져와서 지도에 설정
        fusedLocationClient.lastLocation.addOnSuccessListener(this, OnSuccessListener { location ->
            if (location != null) {
                // 위치를 가져왔을 때
                val userLocation = LatLng(location.latitude, location.longitude)


                // NaverMapOptions 설정
                val options = NaverMapOptions()
                    .camera(CameraPosition(userLocation, 16.0))
                    .mapType(NaverMap.MapType.Terrain)

                val mapFragment = MapFragment.newInstance(options)

                // FragmentTransaction을 사용하여 MapFragment를 Activity에 추가
                supportFragmentManager.beginTransaction().replace(binding.naverMap.id, mapFragment)
                    .commit()
                mapFragment.getMapAsync(this)


                // NaverMap 객체 가져오기
                mapFragment.getMapAsync { naverMap ->
                    // 마커 추가
                    val marker = Marker().apply {
                        position = userLocation
                        map = naverMap  // 마커를 NaverMap에 추가
                    }

                    // 마커 클릭 이벤트 리스너 설정
                    marker.setOnClickListener {

                        // BottomSheet을 완전히 펼치기
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

                        Toast.makeText(this@LocationTest3Activity, "마커 클릭됨!", Toast.LENGTH_SHORT).show()
                        true // 이벤트가 소비되었음을 알림
                    }
                }

                Log.d("LOCATION_DEBUG", "User location: $userLocation")


            } else {
                // 위치를 가져올 수 없을 때
                Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show()
            }

            //        시트 숨기기
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN


            //          위치 정보 제공자 설정
            locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        })
    }

        override fun onSupportNavigateUp(): Boolean {
            finish()
            return true
        }

        override fun onMapReady(map: NaverMap) {

            naverMap = map
            naverMap.locationSource = locationSource

            // 지도가 클릭 되면 onMapClick() 콜백 메서드가 호출 되며, 파라미터로 클릭된 지점의 화면 좌표와 지도 좌표가 전달 된다.
            naverMap.setOnMapClickListener { point, coord ->

                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

                Toast.makeText(
                    this, "${coord.latitude}, ${coord.longitude}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            // 기본 UI 설정 활성화
            val uiSettings = naverMap.uiSettings
            uiSettings.isLocationButtonEnabled = true // 기본 위치 버튼 숨김


            // 위치 권한 확인
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                naverMap.locationTrackingMode = LocationTrackingMode.Follow

                // 현재 위치 가져와서 마커 추가
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        val userLocation = LatLng(location.latitude, location.longitude)
                        addNearbyStationsMarkers(userLocation, naverMap)
                    }
                }

            } else {
                // 권한 요청
                ActivityCompat.requestPermissions(this, LOCATION_PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE)
            }

        }

    private fun addNearbyStationsMarkers(userLocation: LatLng, naverMap: NaverMap) {
        val nearbyStations = subwayStations.filter { station ->
            val results = FloatArray(1)
            Location.distanceBetween(
                userLocation.latitude, userLocation.longitude,
                station.latitude, station.longitude,
                results
            )
            results[0] <= 1000 // 1km 이내인지 확인
        }

        // 마커 추가
        nearbyStations.forEach { station ->
          val markers =  Marker().apply {
                position = station
                map = naverMap
            }
            markers.setOnClickListener {

                // BottomSheet을 완전히 펼치기
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                Toast.makeText(this@LocationTest3Activity, "마커 클릭됨!", Toast.LENGTH_SHORT).show()
                true // 이벤트가 소비되었음을 알림
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





//                    Log.d("LOCATION_DEBUG", "User location: $userLocation")
//
//                    // 위치 오버레이 설정
//                    val locationOverlay = naverMap.locationOverlay
//                    locationOverlay.isVisible = true
//                    locationOverlay.icon = OverlayImage.fromResource(R.drawable.location_overlay_icon)
//                    locationOverlay.position = userLocation
//                    locationOverlay.iconWidth = 80
//                    locationOverlay.iconHeight = 80


//import android.Manifest
//import android.content.pm.PackageManager
//import android.os.Bundle
//import android.util.Log
//import android.view.Gravity
//import android.view.View
//import android.widget.ArrayAdapter
//import android.widget.EditText
//import android.widget.LinearLayout
//import android.widget.Spinner
//import android.widget.Toast
//import androidx.activity.enableEdgeToEdge
//import androidx.annotation.RequiresPermission
//import androidx.appcompat.app.AppCompatActivity
//import androidx.appcompat.widget.Toolbar
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import com.example.app.R
//import com.example.app.databinding.ActivityTest3Binding
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.tasks.OnSuccessListener
//import com.google.android.material.bottomsheet.BottomSheetBehavior
//import com.naver.maps.geometry.LatLng
//import com.naver.maps.map.CameraPosition
//import com.naver.maps.map.CameraUpdate
//import com.naver.maps.map.LocationTrackingMode
//import com.naver.maps.map.MapFragment
//import com.naver.maps.map.NaverMap
//import com.naver.maps.map.NaverMapOptions
//import com.naver.maps.map.NaverMapSdk
//import com.naver.maps.map.OnMapReadyCallback
//import com.naver.maps.map.util.FusedLocationSource
//
//
//class LocationTest3Activity : AppCompatActivity(), OnMapReadyCallback {
//
//    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
//
//    private val binding by lazy { ActivityTest3Binding.inflate(layoutInflater) }
//
//    private lateinit var naverMap: NaverMap
//    private lateinit var locationSource: FusedLocationSource
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        var binding = ActivityTest3Binding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//
//        // 툴바 가져오기
//        val toolbar = findViewById<Toolbar>(R.id.toolbar)
//
//        // 기존에 있던 Toolbar 안 내용 모두 제거
//        toolbar.removeAllViews()
//        toolbar.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
//
//        val searchContainer = LinearLayout(this).apply {
//            orientation = LinearLayout.HORIZONTAL
//            gravity = Gravity.CENTER_VERTICAL
//
//            layoutParams = Toolbar.LayoutParams(
//                Toolbar.LayoutParams.MATCH_PARENT,
//                Toolbar.LayoutParams.WRAP_CONTENT
//            ).apply {
//                gravity = Gravity.CENTER_VERTICAL
//                marginEnd = 80
//                marginStart = 80
//            }
//        }
//
//        val titleEditView = EditText(this).apply {
//            hint = "검색어를 입력해 주세요"
//            setHintTextColor(resources.getColor(android.R.color.darker_gray))
//            setTextColor(resources.getColor(android.R.color.black))
//            textSize = 16f
//            background = ContextCompat.getDrawable(
//                this@LocationTest3Activity,
//                R.drawable.border_box
//            )
//            layoutParams = LinearLayout.LayoutParams(
//                0,
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                1f
//            )
//        }
//
//
//        val searchSpinner = Spinner(this).apply {
//            layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            ).apply {
//                marginStart = 16
//            }
//
//            val spinnerAdapter = ArrayAdapter(
//                this@LocationTest3Activity,
//                android.R.layout.simple_spinner_dropdown_item,
//                listOf("300m", "500m", "1km", "2km")
//            )
//            adapter = spinnerAdapter
//        }
//
//        searchContainer.addView(titleEditView)
//        searchContainer.addView(searchSpinner)
//
//        toolbar.addView(searchContainer)
//
//        // 툴바 설정
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼
//
//        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//
//        // 네이버 클라우드 플랫폼 API 키 설정
//        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient("s06jicv68m")
//
//        // 지도 뷰 가져오기
//        val mapFragment = supportFragmentManager.findFragmentById(R.id.naver_map) as MapFragment?
//            ?: MapFragment.newInstance().also {
//                supportFragmentManager.beginTransaction().add(R.id.naver_map, it).commit()
//            }
//        mapFragment.getMapAsync(this)
//
//        // 위치 정보 제공자 설정
//        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
//
//    }
//
//    override fun onMapReady(map: NaverMap) {
//        naverMap = map
//
//        // 위치 소스 설정
//        naverMap.locationSource = locationSource
//
//        // 내 위치를 기본 위치로 설정
//        moveToCurrentLocation()
//
//    }
//
//    private fun moveToCurrentLocation() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
//            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//
//            // 위치 추적 모드 활성화
//            naverMap.locationTrackingMode = LocationTrackingMode.Follow
//
//            // 현재 위치 가져오기
//            val location = locationSource.lastLocation
//            if (location != null) {
//                val userLocation = LatLng(location.latitude, location.longitude)
//                val cameraUpdate = CameraUpdate.scrollTo(userLocation)
//
//                naverMap.moveCamera(cameraUpdate)
//            }
//        } else {
//            ActivityCompat.requestPermissions(this, LOCATION_PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE)
//        }
//    }
//
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
//            // 위치 소스에 권한 처리 결과 전달
//            if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
//                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
//                    moveToCurrentLocation()
//                }
//                return
//            }
//        }
//    }
//
//    companion object {
//        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
//        private val LOCATION_PERMISSIONS = arrayOf(
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION
//        )
//    }
//
//
//    override fun onSupportNavigateUp(): Boolean {
//        finish()
//        return true
//    }
//}
