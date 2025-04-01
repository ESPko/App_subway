package com.example.app.jsy

import android.os.Bundle
import android.text.Html
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.util.Log
import android.view.Gravity
import android.widget.AbsListView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.app.databinding.ActivityStationTimeTableBinding
import com.example.app.dto.StationInfoList
import com.example.app.dto.StationScheduleResponse
import com.example.app.dto.TrainResponse
import com.example.app.retrofit.AppServerClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StationTimeTableActivity : AppCompatActivity() {

  private val binding: ActivityStationTimeTableBinding by lazy {
    ActivityStationTimeTableBinding.inflate(layoutInflater)
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
      text = "역 정보"
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

    Log.d("csy","몇호선인가요? : $lineNumber")
    when(lineNumber){
      1 -> {
        binding.lineColor.setBackgroundColor(ContextCompat.getColor(this, R.color.lineColor_1))
        binding.btnCenter.setTextColor(ContextCompat.getColor(this,R.color.lineColor_1))
      }
      2 -> {
        binding.lineColor.setBackgroundColor(ContextCompat.getColor(this, R.color.lineColor_2))
        binding.btnCenter.setTextColor(ContextCompat.getColor(this,R.color.lineColor_2))
      }
      3 -> {
        binding.lineColor.setBackgroundColor(ContextCompat.getColor(this, R.color.lineColor_3))
        binding.btnCenter.setTextColor(ContextCompat.getColor(this,R.color.lineColor_3))
      }
      4 -> {
        binding.lineColor.setBackgroundColor(ContextCompat.getColor(this, R.color.lineColor_4))
        binding.btnCenter.setTextColor(ContextCompat.getColor(this,R.color.lineColor_4))
      }
    }

    CurrentTime()
    loadData(scode)
    loadTrainData(scode)
    loadStationInfo(scode)

    binding.tvLeft.setOnClickListener {
      scode-- // 값 하나 감소
      CurrentTime()
      loadData(scode)
      loadTrainData(scode)
      loadStationInfo(scode)
      loadStationSheet(scode)
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
      loadData(scode)
      loadTrainData(scode)
      loadStationInfo(scode)
      loadStationSheet(scode)
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
    // 두 ListView 스크롤 동기화
    syncListViewScroll()
  }

  private fun syncListViewScroll() {
    // 첫 번째 ListView (Up Station) 스크롤 리스너
    binding.listUpStation.setOnScrollListener(object : AbsListView.OnScrollListener {
      override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
        // 스크롤 상태 변경 시 처리할 로직 (필요하다면 구현)
      }

      override fun onScroll(
        view: AbsListView?,
        firstVisibleItem: Int,
        visibleItemCount: Int,
        totalItemCount: Int
      ) {
        // 첫 번째 ListView의 첫 번째 보이는 항목의 위치를 두 번째 ListView에 반영
        binding.listDownStation.setSelection(firstVisibleItem)
      }
    })

    // 두 번째 ListView (Down Station) 스크롤 리스너
    binding.listDownStation.setOnScrollListener(object : AbsListView.OnScrollListener {
      override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
        // 스크롤 상태 변경 시 처리할 로직 (필요하다면 구현)
      }

      override fun onScroll(
        view: AbsListView?,
        firstVisibleItem: Int,
        visibleItemCount: Int,
        totalItemCount: Int
      ) {
        // 두 번째 ListView의 첫 번째 보이는 항목의 위치를 첫 번째 ListView에 반영
        binding.listUpStation.setSelection(firstVisibleItem)
      }
    })
  }

  private fun CurrentTime() {
    val date = Date()  // 현재 시스템 시간 가져오기
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    val formattedTime = sdf.format(date)

    // : 기호 제거
    currentTime = formattedTime.replace(":", "")
    Log.d("csy", "Current Time: $currentTime")
  }

  private fun loadData(scode: Int) {
    val call = api.getCategoryName(scode = scode.toString())
    retrofitResponse(call, scode)
  }

  private fun loadTrainData(scode: Int) {
    val call = api.getTrainTimeAndName(scode = scode.toString(), sttime = currentTime, day = "1")
    retrofitResponse2(call)
  }

  private fun loadStationInfo(scode: Int){
    val call = api.getStationInfo(scode = scode.toString())
    retrofitResponse3(call)
  }

  private fun loadStationSheet(scode: Int){
    val call = api.getStationSheet(scode = scode.toString())
    retrofitResponse4(call)
  }

  private fun retrofitResponse(call: Call<List<Station>>, scode: Int) {
    call.enqueue(object : Callback<List<Station>> {
      override fun onResponse(call: Call<List<Station>>, res: Response<List<Station>>) {
        if (res.isSuccessful) {
          val result = res.body()
          Log.d("csy", "지하철역 시간표 : $result")
          result?.let {
            if (it.isNotEmpty()) {
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

  private fun retrofitResponse2(call: Call<TrainResponse>) {
    call.enqueue(object : Callback<TrainResponse> {
      override fun onResponse(call: Call<TrainResponse>, res: Response<TrainResponse>) {
        if (res.isSuccessful) {
          val result = res.body()
          Log.d("csy", "scode 기준 열차 도착 남은 정보 : $result")
        } else {
          Log.d("csy", "송신 실패, 응답 코드: ${res.code()} 메시지: ${res.message()}")
        }
      }

      override fun onFailure(call: Call<TrainResponse>, t: Throwable) {
        Log.d("csy", "message : ${t.message}")
      }
    })
  }

  private fun retrofitResponse3(call: Call<List<StationInfoList>>) {
    call.enqueue(object : Callback<List<StationInfoList>> {
      override fun onResponse(call: Call<List<StationInfoList>>, res: Response<List<StationInfoList>>) {
        if (res.isSuccessful) {
          val result = res.body()
          Log.d("csy", "시설 정보 : $result")
        } else {
          Log.d("csy", "송신 실패, 응답 코드: ${res.code()} 메시지: ${res.message()}")
        }
      }
      override fun onFailure(call: Call<List<StationInfoList>>, t: Throwable) {
        Log.d("csy", "message : ${t.message}")
      }
    })
  }

  private fun retrofitResponse4(call: Call<StationScheduleResponse>) {
    call.enqueue(object : Callback<StationScheduleResponse> {
      override fun onResponse(call: Call<StationScheduleResponse>, res: Response<StationScheduleResponse>) {
        if (res.isSuccessful) {
          val stationScheduleResponse = res.body()
          Log.d("csy", "timeups: ${stationScheduleResponse?.timeups}")
          Log.d("csy", "timedowns: ${stationScheduleResponse?.timedowns}")

          stationScheduleResponse?.timeups?.let {
            val upAdapter = TimeDataAdapter(this@StationTimeTableActivity, it)
            binding.listUpStation.adapter = upAdapter
          }
          stationScheduleResponse?.timedowns?.let {
            val downAdapter = TimeDataAdapter(this@StationTimeTableActivity, it)
            binding.listDownStation.adapter = downAdapter
          }

        } else {
          Log.d("csy", "송신 실패, 응답 코드: ${res.code()} 메시지: ${res.message()}")
        }
      }

      override fun onFailure(call: Call<StationScheduleResponse>, t: Throwable) {
        Log.d("csy", "message : ${t.message}")
      }
    })
  }

  override fun onSupportNavigateUp(): Boolean {
    finish()
    return true
  }
}
