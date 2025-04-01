package com.example.app.jsy

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app.R
import com.example.app.databinding.ActivityStationDetailBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.os.postDelayed
import androidx.lifecycle.lifecycleScope
import com.example.app.dto.StationInfoList
import com.example.app.dto.StationScheduleResponse
import com.example.app.dto.TrainResponse
import com.example.app.retrofit.AppServerClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

  private var stationName: String? = null
  private var lineNumber: Int = 0
  var currentTime: String = ""
  val api = AppServerClass.instance

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContentView(binding.root)
    // 툴바 가져오기
    val toolbar = findViewById<Toolbar>(R.id.toolbar)

    // 기존에 있던 Toolbar 안 내용 모두 제거
    toolbar.removeAllViews()

    // 변경될 툴바 내용
    val titleTextView = TextView(this).apply {
      text = "역 검색"
      setTextColor(resources.getColor(android.R.color.white))
      textSize = 18f
      layoutParams = Toolbar.LayoutParams(
        Toolbar.LayoutParams.WRAP_CONTENT,
        Toolbar.LayoutParams.WRAP_CONTENT
      ).apply {
        gravity = Gravity.CENTER
      }
    }


    toolbar.addView(titleTextView)

    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼
    supportActionBar?.setDisplayShowTitleEnabled(false) // 기본 타이틀 제거

    toolbar.navigationIcon?.setTint(ContextCompat.getColor(this, android.R.color.white))

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

    binding.btnStationTimeTable.setOnClickListener {
      val intent = android.content.Intent(this, StationTimeTableActivity::class.java)
      intent.putExtra("scode", scode)  // 역 코드
      intent.putExtra("name", stationName)  // 역 이름
      intent.putExtra("line", lineNumber)  // 역 노선

      startActivity(intent)
    }

    CurrentTime()

    loadAllData(scode)

    setupLineColor(lineNumber)

    binding.tvLeft.setOnClickListener {
      scode-- // 값 하나 감소
      CurrentTime()
      loadAllData(scode)
      when(scode){
        95, 201,301,401 -> {
          binding.tvLeft.isEnabled = false
          binding.tvLeftLt.text = " "
        }
        else -> {
          binding.tvRight.isEnabled = true
          binding.tvRightGt.text = Html.fromHtml("&gt;")
        }
      }
    }

    binding.tvRight.setOnClickListener {
      scode++ // 값 하나 증가
      CurrentTime()
      loadAllData(scode)
      when(scode){
        134,243,317,414 -> {
          binding.tvRight.isEnabled = false
          binding.tvRightGt.text = " "
        }
        else -> {
          binding.tvLeft.isEnabled = true
          binding.tvLeftLt.text = Html.fromHtml("&lt;")
        }
      }
    }

  }

  private fun CurrentTime() {
    val date = Date()  // 현재 시스템 시간 가져오기
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    val formattedTime = sdf.format(date)

    // : 기호 제거
    currentTime = formattedTime.replace(":", "")
    Log.d("csy", "Current Time: $currentTime")
  }
  private fun loadAllData(scode: Int) {
    lifecycleScope.launch {
      try {
        // 비동기 데이터 로딩
        val loadDataResult = async { loadData(scode) }
        val loadTrainDataResult = async { loadTrainData(scode) }
        val loadStationInfoResult = async { loadStationInfo(scode) }

        // 모든 비동기 작업이 완료될 때까지 기다리기
        loadTrainDataResult.await()
        loadDataResult.await()
        loadStationInfoResult.await()

        Log.d("csy", "모든 데이터 로딩 완료")

      } catch (e: Exception) {
        // 오류 발생 시
        Log.e("csy", "데이터 로딩 중 오류 발생: ${e.message}")
      }
    }
  }

  private fun loadData(scode: Int) {
    val call = api.getCategoryName(scode = scode.toString())
    retrofitResponse(call, scode)
  }
  private fun loadTrainData(scode: Int) {
    lifecycleScope.launch{
      try{
        val response = withContext(Dispatchers.IO){
          api.getTrainTimeAndName(scode = scode.toString(), sttime = currentTime, day = "1")
        }
        retrofitResponse2(response)
      }catch (e: Exception){
        Log.d("csy", "Error: ${e.message}")
      }
    }
  }

  private fun loadStationInfo(scode: Int){
    val call = api.getStationInfo(scode = scode.toString())
    retrofitResponse3(call)
  }

  private fun setupLineColor(lineNumber: Int) {
    when (lineNumber) {
      1 -> {
        binding.lineColor.setBackgroundColor(ContextCompat.getColor(this, R.color.lineColor_1))
        binding.btnCenter.setTextColor(ContextCompat.getColor(this, R.color.lineColor_1))
      }
      2 -> {
        binding.lineColor.setBackgroundColor(ContextCompat.getColor(this, R.color.lineColor_2))
        binding.btnCenter.setTextColor(ContextCompat.getColor(this, R.color.lineColor_2))
      }
      3 -> {
        binding.lineColor.setBackgroundColor(ContextCompat.getColor(this, R.color.lineColor_3))
        binding.btnCenter.setTextColor(ContextCompat.getColor(this, R.color.lineColor_3))
      }
      4 -> {
        binding.lineColor.setBackgroundColor(ContextCompat.getColor(this, R.color.lineColor_4))
        binding.btnCenter.setTextColor(ContextCompat.getColor(this, R.color.lineColor_4))
      }
    }
  }

  private fun retrofitResponse(call: Call<List<Station>>, scode: Int) {
    call.enqueue(object : Callback<List<Station>> {
      override fun onResponse(call: Call<List<Station>>, res: Response<List<Station>>) {
        if (res.isSuccessful) {
          val result = res.body()
          Log.d("csy", "역 정보 리스트 : $result")
          result?.let {
            if (it.isNotEmpty()) {
//              Log.d("csy","RetrofitResponse 안에서의 scode 값 : $scode")
              when (scode) {
                95, 201,301,401 -> {
                  binding.tvLeft.text = "종착"
                  binding.btnCenter.text = it.getOrNull(0)?.name
                  binding.tvRight.text = it.getOrNull(1)?.name
                }
                134,243,317,414 -> {
                  binding.tvLeft.text = it.getOrNull(0)?.name
                  binding.btnCenter.text = it.getOrNull(1)?.name
                  binding.tvRight.text = "종착"
                }
                else -> {
                  binding.tvLeft.text = it.getOrNull(0)?.name
                  binding.btnCenter.text = it.getOrNull(1)?.name
                  binding.tvRight.text = it.getOrNull(2)?.name
                }
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

  private fun updateStationUI(
    stationName: String,
    times: List<Int?>,
    titleTextViews: List<TextView>,
    timeTextViews: List<TextView>
  ) {
    val timeStrings = times.map { time ->
      when {
        time == null -> "데이터 없음"
        time == 0 -> "곧 도착"
        else -> "$time 분 후"
      }
    }
    // titleTextViews와 timeTextViews를 동시에 업데이트
    titleTextViews.forEach { it.text = stationName }

    timeTextViews.forEachIndexed { index, textView ->
      textView.text = timeStrings.getOrElse(index) { "데이터 없음" }
    }
  }

  private suspend fun retrofitResponse2(response: TrainResponse) {
    withContext(Dispatchers.Main) {
      Log.d("csy", "scode 기준 열차 도착 남은 정보 : $response")
      val stations = listOf(
        "노포" to response.노포,
        "다대포해수욕장" to response.다대포해수욕장,
        "양산" to response.양산,
        "장산" to response.장산,
        "수영" to response.수영,
        "대저" to response.대저,
        "미남" to response.미남,
        "안평" to response.안평
      )

      stations.forEach { (stationName, times) ->
        times?.let {
          val titleTextViews = when (stationName) {
            "노포", "장산", "대저", "안평" -> listOf(binding.upTitleText, binding.upTitleText1, binding.upTitleText2, binding.upTitleText3)
            else -> listOf(binding.downTitleText, binding.downTitleText1, binding.downTitleText2, binding.downTitleText3)
          }

          val timeTextViews = when (stationName) {
            "노포", "장산", "대저", "안평" -> listOf(binding.upTimeText1, binding.upTimeText2, binding.upTimeText3)
            else -> listOf(binding.downTimeText1, binding.downTimeText2, binding.downTimeText3)
          }

          updateStationUI(stationName, it, titleTextViews, timeTextViews)
        }
      }
    }
  }




  private fun retrofitResponse3(call: Call<List<StationInfoList>>) {
    call.enqueue(object : Callback<List<StationInfoList>> {
      override fun onResponse(call: Call<List<StationInfoList>>, res: Response<List<StationInfoList>>) {
        if (res.isSuccessful) {
          val result = res.body()
          Log.d("csy", "시설 정보 : $result")


          result?.forEach {
            binding.tvDoor.text = it.door
            binding.tvToilet.text = it.toilet
            binding.tvAcross.text = it.across
            binding.tvFlatform.text = it.flatform
            binding.tvStorage.text = it.storage
            binding.tvUtil.text = it.util
            binding.tvAddr.text = it.address
            binding.tvTel.text = it.number
          }

        } else {
          Log.d("csy", "송신 실패, 응답 코드: ${res.code()} 메시지: ${res.message()}")
        }
      }
      override fun onFailure(call: Call<List<StationInfoList>>, t: Throwable) {
        Log.d("csy", "message : ${t.message}")
      }
    })
  }




  override fun onSupportNavigateUp(): Boolean {
    finish()
    return true
  }
}
