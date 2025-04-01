package com.example.app.jsy

import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.app.R
import com.example.app.dto.TrainResponse
import com.example.app.retrofit.AppServerClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


//data class Station(
//  val name: String,
//  val scode: Int,
//  val line: Int
//)
data class Train(
  val hour: Int,
  val time: Int,
  val day: String,
  val updown: String,
  val endcode: String
)

class Line1Fragment : Fragment() {
  // TODO: Rename and change types of parameters
  private var param1: String? = null
  private var param2: String? = null

  // 텍스트뷰 변수 선언
  private lateinit var tvLeft: TextView
  private lateinit var btnCenter: TextView
  private lateinit var tvRight: TextView
  private lateinit var tvLeftLt: TextView
  private lateinit var tvRightGt: TextView

  // scode 받아오는 초기값
  private var scode: Int = 103
  private var stationName: String? = null  // For station name (optional)
  private var lineNumber: Int = 0  // Line number (optional)
  var currentTime: String = ""

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      param1 = it.getString(ARG_PARAM1)
      param2 = it.getString(ARG_PARAM2)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val binding = inflater.inflate(R.layout.fragment_line1, container, false)

    // 텍스트뷰 초기화 설정
    tvLeft = binding.findViewById(R.id.tv_left)
    btnCenter = binding.findViewById(R.id.btn_center)
    tvRight = binding.findViewById(R.id.tv_right)
    tvLeftLt = binding.findViewById(R.id.tv_left_lt)
    tvRightGt = binding.findViewById(R.id.tv_right_gt)

    // Retrieve the arguments (scode, name, and line) from the fragment's arguments
    arguments?.let {
      scode = it.getInt("scode", 96)  // Default to 96 if not found
      stationName = it.getString("name", "")  // Default to empty string if not found
      lineNumber = it.getInt("line", 0)  // Default to 0 if not found
    }
    Log.d("csy", "Received scode: $scode, stationName: $stationName, lineNumber: $lineNumber")


    CurrentTime()
    loadData(scode)
//    loadTrainData(scode)
    tvLeft.setOnClickListener{
      scode-- // 값 하나 감소
      loadData(scode)
      if(scode == 95){
        tvLeft.isEnabled = false // tvLeft 비활성화
        tvLeftLt.text = " "
      }else{
        tvRight.isEnabled = true // tvRight 활성화
        tvRightGt.text = Html.fromHtml("&gt;")
      }

    }
    tvRight.setOnClickListener{
      scode++ // 값 하나 증가
      loadData(scode)
      if(scode == 134){
        tvRight.isEnabled = false // tvRight 비활성화
        tvRightGt.text = " "
      }else{
        tvLeft.isEnabled = true // tvLeft 활성화
        tvLeftLt.text = Html.fromHtml("&lt;")
      }
    }

    return binding
  }

  companion object {
    @JvmStatic
    fun newInstance(param1: String, param2: String) =
      Line1Fragment().apply {
        arguments = Bundle().apply {
          putString(ARG_PARAM1, param1)
          putString(ARG_PARAM2, param2)
        }
      }
  }

  private fun loadData(scode: Int){
    val api = AppServerClass.instance
    val call = api.getCategoryName(scode = scode.toString())
    retrofitResponse(call)
  }

  private fun CurrentTime(){
    val date = Date()  // 현재 시스템 시간 가져오기

    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    val formattedTime = sdf.format(date)

    // : 기호 제거

    currentTime = formattedTime.replace(":", "")

    Log.d("csy", "Current Time: $currentTime")
  }

//  private fun loadTrainData(scode: Int){
//    val api = AppServerClass.instance
//    val call = api.getTrainTimeAndName(scode = scode.toString(), sttime = currentTime, day = "1")
//    retrofitResponse2(call)
//  }

  private fun retrofitResponse(call: Call<List<Station>>) {
    call.enqueue(object : Callback<List<Station>> {
      override fun onResponse(call: Call<List<Station>>, res: Response<List<Station>>) {
        if (res.isSuccessful) {
          val result = res.body()
          Log.d("csy", "result : $result")

          // 응답 받은 데이터가 있을 경우 TextView에 설정
          result?.let {
            // 여러 개의 데이터 중 첫 번째 항목의 name을 텍스트뷰에 설정하는 예시
            if (it.isNotEmpty()) {
              if(scode == 95){
                tvLeft.text = "종착"
                btnCenter.text = it.getOrNull(0)?.name ?: "종착"
                tvRight.text = it.getOrNull(1)?.name ?: "종착"
              }else if(scode == 134){
                tvLeft.text = it.getOrNull(0)?.name ?: "종착"
                btnCenter.text = it.getOrNull(1)?.name ?: "종착"
                tvRight.text = "종착"
              }else{
                tvLeft.text = it.getOrNull(0)?.name ?: "종착"
                btnCenter.text = it.getOrNull(1)?.name ?: "종착"
                tvRight.text = it.getOrNull(2)?.name ?: "종착"
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
          result?.let {
            Log.d("csy", "result ${it.노포}")
            Log.d("csy", "result ${it.다대포해수욕장}")
          }

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