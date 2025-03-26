package com.example.app.min


import android.health.connect.datatypes.ExerciseRoute.Location
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app.R
import com.example.app.databinding.ActivityTest3Binding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource


class LocationTest3Activity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var binding = ActivityTest3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        locationSource = FusedLocationSource(
            this,
            LOCATION_PERMISSION_REQUEST_CODE
        )

        checkLocationEnabled()

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
        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient("s06jicv68m")

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (
            locationSource.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
            )
        )    if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
                Log.d("LocationTest", "위치 권한 거부됨")
            } else {
                naverMap.locationTrackingMode = LocationTrackingMode.Follow  // 내 위치 따라가기
                Log.d("LocationTest", "위치 권한 허용됨")
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        // 내 위치 추적 모드 설정
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

        Log.d("LocationTest", "네이버 지도 초기화 완료")

        // 내 위치 버튼 활성화 (UI 설정)
        val uiSettings = naverMap.uiSettings
        uiSettings.isLocationButtonEnabled = true


        // 현재 위치 가져오기
        val locationOverlay = naverMap.locationOverlay
        locationOverlay.isVisible = true
    }

    private fun checkLocationEnabled() {
        val locationManager = getSystemService(LOCATION_SERVICE) as android.location.LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)

        if (!isGpsEnabled) {
            val intent = android.content.Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }


    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }


}