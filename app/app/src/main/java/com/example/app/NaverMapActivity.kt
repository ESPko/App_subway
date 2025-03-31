package com.example.app

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app.databinding.ActivityNaverMapBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.overlay.Marker

class NaverMapActivity : AppCompatActivity() , OnMapReadyCallback {
  private val binding by lazy { ActivityNaverMapBinding.inflate(layoutInflater) }

  private lateinit var naverMap: NaverMap
  private lateinit var locationSource: FusedLocationSource

  private lateinit var fusedLocationClient: FusedLocationProviderClient


  @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContentView(binding.root)
    ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
      insets
    }

//    NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient("s5lqkqcw4y")

    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    fusedLocationClient.lastLocation.addOnSuccessListener(this, OnSuccessListener { location ->

      if (location != null) {

        // 위치를 가져왔을 때
        val userLocation = LatLng(location.latitude, location.longitude)

        // NaverMapOptions 설정
        val options = NaverMapOptions()
          .camera(CameraPosition(userLocation, 16.0))
          .mapType(NaverMap.MapType.Terrain)

      }

    })

    NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient("s06jicv68m")

    // 지도 뷰 가져오기
    val mapFragment = supportFragmentManager.findFragmentById(R.id.naver_map) as MapFragment?
      ?: MapFragment.newInstance().also {
        supportFragmentManager.beginTransaction().add(R.id.naver_map, it).commit()
      }
    mapFragment.getMapAsync(this)


    // 위치 정보 제공자 설정
    locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)


  }

  override fun onMapReady(map: NaverMap) {

    naverMap = map
    naverMap.locationSource = locationSource

    // 위치 권한이 있을 경우 바로 내 위치로 이동
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
      ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

      naverMap.locationTrackingMode = LocationTrackingMode.Follow

      // 현재 위치 가져오기 (비동기 처리)
      val location = locationSource.lastLocation
      if (location != null) {

        val userLocation = LatLng(location.latitude, location.longitude)
//        카메라 위치 이동
        val cameraUpdate = CameraUpdate.scrollTo(userLocation)
        naverMap.moveCamera(cameraUpdate)

        Log.d("MAP", "현재 위치: ${userLocation.latitude}, ${userLocation.longitude}")
        Toast.makeText(this, "현재 위치: ${userLocation.latitude}, ${userLocation.longitude}", Toast.LENGTH_SHORT).show()

        // 마커 추가
        val marker = Marker().apply {
          position = userLocation
        }
        marker.map = naverMap

        Log.d("MAP", "마커가 추가됨: ${marker.position.latitude}, ${marker.position.longitude}")

      }
    } else {
      // 권한이 없으면 요청
      ActivityCompat.requestPermissions(this, LOCATION_PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE)

    }

    // 지도가 클릭 되면 onMapClick() 콜백 메서드가 호출 되며, 파라미터로 클릭된 지점의 화면 좌표와 지도 좌표가 전달 된다.
    naverMap.setOnMapClickListener { point, coord ->
      Toast.makeText(
        this, "${coord.latitude}, ${coord.longitude}",
        Toast.LENGTH_SHORT
      ).show()
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
