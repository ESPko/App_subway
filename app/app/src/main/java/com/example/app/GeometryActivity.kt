package com.example.app

import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app.databinding.ActivityGeometryBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class GeometryActivity : AppCompatActivity() {
  private val binding by lazy { ActivityGeometryBinding.inflate(layoutInflater) }
  // 플랫폼 API에서 제공되는 위치정보를 제어하는 서비스 객체
  private lateinit var locationManager: LocationManager

  private lateinit var fusedLocationClient: FusedLocationProviderClient


  private val locationPermissionRequest = registerForActivityResult(
    ActivityResultContracts.RequestMultiplePermissions()
  ) { permissions ->
    if (permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true
      || permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
    ) {
      Log.d("fullstack503", "위치 권한 승인")
      Toast.makeText(this, "위치 권한 승인", Toast.LENGTH_SHORT).show()
    } else {
      Log.d("fullstack503", "위치 권한 거부")
      Toast.makeText(this, "위치 권한 거부", Toast.LENGTH_SHORT).show()
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContentView(binding.root)
    ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
      insets
    }
    // 사용자에게 권한 획득 요청
    locationPermissionRequest.launch(
      arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
      )
    )
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


    initEventListener()
  }

  private fun initEventListener() {
    binding.btnPlatformApi.setOnClickListener {
      // 권한 확인 안쓰면 오류남
      if (ActivityCompat.checkSelfPermission(
          this,
          android.Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
      ) {
        Log.d("fullstack503", "위치 권한 없음")
        Toast.makeText(this, "위치 권한 없음", Toast.LENGTH_SHORT).show()
      } else {
        // 시스템 서비스를 사용하여 위치 정보 관리 객체를 생성
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        // 전체 위치정보 제공 기기 출력
        val providers = locationManager.allProviders
        var result = ""

        for (provider in providers) {
          result += "$provider, "
        }
        Log.d("fullstack503", "전체 위치 제공자 : $result")
        // 사용 가능한 위치 제공자 모두 출력
        val enableProviders = locationManager.getProviders(true)
        result = ""
        for (provider in enableProviders) {
          result += "$provider, "
        }
        Log.d("fullstack503", "사용 가능한 위치 제공자 : $result")

        // 지정한 위치 제공자를 통해서 위치정보를 1회 가져옴
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        location?.let {
          val latitude = it.latitude  // 위도
          val longitude = it.longitude  // 경도
          val accuracy = it.accuracy  // 정확도
          val time = it.time  // 시간
          Log.d(
            "fullstack503",
            "플랫폼 API 위치 -> 위도 : $latitude, 경도 : $longitude, 정확도 : $accuracy, 시간 : $time"
          )
          Toast.makeText(
            this,
            "플랫폼 API 위치 -> 위도 : $latitude, 경도 : $longitude, 정확도 : $accuracy, 시간 : $time",
            Toast.LENGTH_SHORT
          ).show()
        } ?: Log.d("fullstack503", "위치 정보 없음")
        val listener: LocationListener = object : LocationListener {
          override fun onLocationChanged(location: Location) {
            Log.d(
              "fullstack503",
              "플랫폼 API 위치(Listener) -> 위도 : ${location.latitude}, 경도 : ${location.longitude}, 정확도${location.accuracy}, 시간 : ${location.time}")
          }
        }
        // locationManager 에 listener 등록
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10_000L, 10f, listener)
      }
    }
    binding.btnGooglePlay.setOnClickListener {
      if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
        Log.d("fullstack503", "권한이 없습니다")
      }else{
        fusedLocationClient.lastLocation
          .addOnSuccessListener { location: Location? ->
            location?.let{
              val latitude = it.latitude  // 위도
              val longitude = it.longitude  // 경도
              val accuracy = it.accuracy  // 정확도
              val time = it.time  // 시간

              Log.d("fullstack503", "구글 플레이 라이브러리 위치 -> 위도 : $latitude, 경도 : $longitude, 정확도 : $accuracy, 시간 : $time")
              Toast.makeText(this, "구글 플레이 라이브러리 위치 -> 위도 : $latitude, 경도 : $longitude, 정확도 : $accuracy, 시간 : $time", Toast.LENGTH_SHORT).show()
            }?:Log.d("fullstack503", "구글 플레이 라이브러리 위치 정보 없음")
          }
          .addOnFailureListener {
            Log.e("fullstack503", "구글 플레이 라이브러리 위치 정보 가져오기 실패", it)
          }
      }
    }
  }
}