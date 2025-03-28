package com.example.app.jsy

import android.os.Bundle
import android.text.Html
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.app.R
import com.example.app.databinding.ActivityStationDetailBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.util.Log
import com.example.app.TrainResponse
import com.example.app.retrofit.AppServerClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class Station(
  val name: String,
  val scode: Int,
  val line: Int
)

class StationDetailActivity : AppCompatActivity() {

  private val binding: ActivityStationDetailBinding by lazy {
    ActivityStationDetailBinding.inflate(layoutInflater)
  }
  private var scode: Int = 0
  private var stationName: String? = null
  private var lineNumber: Int = 0

  var currentTime: String = ""



  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContentView(binding.root)
    // 시스템 바 영역을 고려하여 뷰 패딩 설정
    ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
      insets
    }
    // Intent에서 전달된 데이터를 받아오기
    var scode = intent.getIntExtra("scode", 0)
    val stationName = intent.getStringExtra("name") ?: ""
    val lineNumber = intent.getIntExtra("line", 0)


    Log.d("csy","scode : $scode")

    CurrentTime()
    loadData(scode)
    loadTrainData(scode)

//    if (savedInstanceState == null) {
//      replaceFragment(Line1Fragment())  // 첫 번째로 1호선 정보 Fragment를 표시
//    }

    binding.tvLeft.setOnClickListener {
      scode-- // 값 하나 감소
      loadData(scode)
      if (scode == 95) {
        binding.tvLeft.isEnabled = false
        binding.tvLeftLt.text = " "
      } else {
        binding.tvRight.isEnabled = true
        binding.tvRightGt.text = Html.fromHtml("&gt;")
      }
    }

    binding.tvRight.setOnClickListener {
      scode++ // 값 하나 증가
      loadData(scode)
      if (scode == 134) {
        binding.tvRight.isEnabled = false
        binding.tvRightGt.text = " "
      } else {
        binding.tvLeft.isEnabled = true
        binding.tvLeftLt.text = Html.fromHtml("&lt;")
      }
    }

  }

//  private fun replaceFragment(fragment: Fragment) {
//    // Fragment에 데이터를 전달하기 위해 Bundle에 넣어서 전달
//    val stationData = Bundle()
//    stationData.putInt("scode", intent.getIntExtra("scode", 100))
//    stationData.putString("name", intent.getStringExtra("name"))
//    stationData.putInt("line", intent.getIntExtra("line", 0))
//
//    fragment.arguments = stationData  // Fragment에 인수로 데이터 전달
//
//    // Fragment 전환
//    supportFragmentManager.beginTransaction()
//      .replace(R.id.fragment_container, fragment)
//      .addToBackStack(null)  // 백스택에 넣어두어 뒤로가기 시 이전 Fragment로 돌아갈 수 있게 함
//      .commit()
//  }

  private fun CurrentTime() {
    val date = Date()  // 현재 시스템 시간 가져오기
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    val formattedTime = sdf.format(date)

    // : 기호 제거
    currentTime = formattedTime.replace(":", "")
    Log.d("csy", "Current Time: $currentTime")
  }
  private fun loadData(scode: Int) {
    Log.d("csy","LoadData 에서의 scode : $scode")
    val api = AppServerClass.instance
    val call = api.getCategoryName(scode = scode.toString())
    retrofitResponse(call)
  }
  private fun loadTrainData(scode: Int) {
    val api = AppServerClass.instance
    val call = api.getTrainTimeAndName(scode = scode.toString(), sttime = currentTime, day = "1")
    retrofitResponse2(call)
  }

  private fun retrofitResponse(call: Call<List<Station>>) {
    call.enqueue(object : Callback<List<Station>> {
      override fun onResponse(call: Call<List<Station>>, res: Response<List<Station>>) {
        if (res.isSuccessful) {
          val result = res.body()
          Log.d("csy", "station Result !!!!!!!!!! : $result")
          result?.let {
            if (it.isNotEmpty()) {
              if(scode == 95){
                binding.tvLeft.text = "종착"
                binding.btnCenter.text = it.getOrNull(0)?.name
                binding.tvRight.text = it.getOrNull(1)?.name
              }else if(scode == 134){
                binding.tvLeft.text = it.getOrNull(0)?.name
                binding.btnCenter.text = it.getOrNull(1)?.name
                binding.tvRight.text = "종착"
              }else{
                binding.tvLeft.text = it.getOrNull(0)?.name
                binding.btnCenter.text = it.getOrNull(1)?.name
                binding.tvRight.text = it.getOrNull(2)?.name
              }

            }
          }
        } else {
          Log.d("csy", "송신 실패, 응답 코드: ${res.code()} 메시지: ${res.message()}")
        }
      }
      override fun onFailure(call: Call<List<Station>>, t: Throwable) {
        Log.d("csy", "message : ${t.message}")
      }
    })
  }

  private fun retrofitResponse2(call: Call<TrainResponse>) {
    call.enqueue(object : Callback<TrainResponse> {
      override fun onResponse(call: Call<TrainResponse>, res: Response<TrainResponse>) {
        if (res.isSuccessful) {
          val result = res.body()
          Log.d("csy", "result : $result")
        } else {
          Log.d("csy", "송신 실패, 응답 코드: ${res.code()} 메시지: ${res.message()}")
        }
      }

      override fun onFailure(call: Call<TrainResponse>, t: Throwable) {
        Log.d("csy", "message : ${t.message}")
      }
    })
  }

}
