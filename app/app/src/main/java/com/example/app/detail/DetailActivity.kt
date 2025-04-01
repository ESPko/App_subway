package com.example.app.detail

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app.MainActivity
import com.example.app.R
import com.example.app.databinding.ActivityDetailBinding
import com.example.app.dto.CategoryDTO
import com.example.app.retrofit.AppServerClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DetailActivity : AppCompatActivity() {

  private val binding: ActivityDetailBinding by lazy {
    ActivityDetailBinding.inflate(layoutInflater)
  }

  private var isFavoriteActive = false
  private var selectedTime = getCurrentTime() // 현재 시간으로 초기화
  private var timeOffset = 0


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContentView(binding.root)



    ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
      insets
    }


    // 초기 화면에서 정보 보이도록 설정
    binding.detailInfoLayout.visibility = View.VISIBLE
    binding.timePickerContainer.visibility = View.GONE

    // Intent로부터 CategoryDTO 객체들을 받아오기
    val selectedDeparture = intent.getParcelableExtra<CategoryDTO>("departure")
    val selectedArrival = intent.getParcelableExtra<CategoryDTO>("arrival")

    Log.d("csy", "Received departure: $selectedDeparture, arrival: $selectedArrival")

    // 액티비티 시작 시 가장 가까운 출발 시간 설정
    selectedDeparture?.let {
      // 출발역 코드로 getClosestTrainTime 호출
      // 출발역 코드로 getClosestTrainTime 호출
      getClosestTrainTime(it.scode.toString())
    }

    selectedArrival?.let {
      // 출발역 코드로 getClosestTrainTime 호출
      // 출발역 코드로 getClosestTrainTime 호출
      getClosestTrainTime(it.scode.toString())
    }


    // 출발역과 도착역 정보를 화면에 표시
    selectedDeparture?.let {
      binding.btnDetailStart1.text = it.name
      binding.btnDetailStart2.text = it.name
      stScode = it.scode.toString()  // 출발역 코드 stScode에 할당
      // 출발역 코드로 getClosestTrainTime 호출
      getClosestTrainTime(it.scode.toString())
    }

    selectedArrival?.let {
      binding.btnDetailArrival1.text = it.name
      binding.btnDetailArrival2.text = it.name
      edScode = it.scode.toString()  // 도착역 코드 edScode에 할당
      // 도착역 코드로 getClosestTrainTime 호출
      getClosestTrainTime(it.scode.toString())
    }

    // 출발역의 lineColor 설정
    selectedDeparture?.let {
      setLineColor(it.line)
    }

    // 출발역과 도착역 정보가 있을 때 API 호출
    selectedDeparture?.let { departure ->
      selectedArrival?.let { arrival ->
        // 이제 name 대신 CategoryDTO 객체를 전달
        getTravelTime(departure, arrival)
        getExchangeCount(departure, arrival)
      }
    }

    // 상단 뒤로가기 버튼
    binding.btnDetailBack.setOnClickListener {
      navigateToMainActivity()
    }

    // 출발역 / 도착역 버튼 클릭
    binding.btnDetailStart1.setOnClickListener {
      navigateToMainActivity()
    }
    binding.btnDetailArrival1.setOnClickListener {
      navigateToMainActivity()
    }

    // 역위치 변경 버튼
    binding.btnDetailChange.setOnClickListener {
      switchStations()
    }

    // 즐겨찾기 버튼
    binding.btnDetailHotlist.setOnClickListener {
      toggleFavorite()
    }

    // 평일, 토요일, 공휴일 선택 - 프래그먼트
//    binding.btnOp1.setOnClickListener {
//      val fragment = BlankFragment1()
//      fragment.show(supportFragmentManager, "blankFragment")
//    }

//    // 시간 설정 버튼
//    binding.btnOp2.text = selectedTime
//    binding.btnOp2.setOnClickListener {
//      openTimePickerFragment()
//    }
//
//    // 출발시간, 도착시간, 막차시간을 선택할 다이얼로그
//    binding.btnOp3.setOnClickListener {
//      openTimeSelectionDialog()
//    }
//
//    // 최소시간/ 최소환승 선택 버튼 클릭
//    binding.btnOp4.setOnClickListener {
//      val fragment = MinTimeDialogFragment()
//      fragment.show(supportFragmentManager, "minTimeDialog")
//    }
//
//    // 출발역과 도착역 정보를 전달하면서 StartBtnFragment 열기
//    binding.btnDetailStart2.setOnClickListener {
//      val startBtnFragment = StartBtnFragment()
//      startBtnFragment.show(supportFragmentManager, "startBtnFragment")
//    }

// 이전열차 버튼 클릭 시 (5분 전 누적)
    binding.previousTrain.setOnClickListener {
      // timeOffset을 -5분씩 줄여서 누적
      timeOffset -= 5
      getUpdatedTrainTime()  // 업데이트된 시간에 대해 API 호출
    }

// 다음열차 버튼 클릭 시 (5분 후 누적)
    binding.nextTrain.setOnClickListener {
      // timeOffset을 +5분씩 증가
      timeOffset += 5
      getUpdatedTrainTime()  // 업데이트된 시간에 대해 API 호출
    }

  }


  private fun setLineColor(line: Int?) {
    val lineColorView = binding.lineColor
    val lineColor = when (line) {
      1 -> {
        lineColorView.text = "1호선"  // 호선 이름을 텍스트로 표시
        R.color.line1Color  // 색상 설정
      }

      2 -> {
        lineColorView.text = "2호선"  // 호선 이름을 텍스트로 표시
        R.color.line2Color  // 색상 설정
      }

      3 -> {
        lineColorView.text = "3호선"  // 호선 이름을 텍스트로 표시
        R.color.line3Color  // 색상 설정
      }

      4 -> {
        lineColorView.text = "4호선"  // 호선 이름을 텍스트로 표시
        R.color.line4Color  // 색상 설정
      }

      else -> {
        lineColorView.text = "알 수 없음"  // 기본 텍스트
        R.color.defaultColor  // 기본 색상
      }
    }
    lineColorView.setBackgroundColor(ContextCompat.getColor(this, lineColor))
  }

  private fun toggleFavorite() {
    if (isFavoriteActive) {
      binding.btnDetailHotlist.clearColorFilter()
    } else {
      binding.btnDetailHotlist.setColorFilter(0xFF00AAFF.toInt(), PorterDuff.Mode.SRC_IN)
    }
    isFavoriteActive = !isFavoriteActive
  }

  private fun switchStations() {
    val startText = binding.btnDetailStart1.text.toString()
    val arrivalText = binding.btnDetailArrival1.text.toString()

    binding.btnDetailStart1.text = arrivalText
    binding.btnDetailArrival1.text = startText
    binding.btnDetailStart2.text = arrivalText
    binding.btnDetailArrival2.text = startText
  }

  private fun navigateToMainActivity() {
    val intent = Intent(this, MainActivity::class.java)
    startActivity(intent)
    finish()
  }

  private fun openTimePickerFragment() {
    val timePickerFragment = TimePickerFragment()
    timePickerFragment.show(supportFragmentManager, "timePicker")
    toggleDetailVisibility(isVisible = false)
  }

  fun onTimeSet(time: String) {
    selectedTime = time
    binding.btnOp2.text = selectedTime
    toggleDetailVisibility(isVisible = true)
  }

  private fun openTimeSelectionDialog() {
    val timeSelectionFragment = TimeSelectionDialogFragment()
    timeSelectionFragment.show(supportFragmentManager, "timeSelectionDialog")
    toggleDetailVisibility(isVisible = false)
  }

  fun onTimeSelected(time: String) {
    selectedTime = time
    binding.btnOp3.text = selectedTime
    toggleDetailVisibility(isVisible = true)
  }

  fun onMinTimeSelected(option: String) {
    binding.btnOp4.text = option
  }

  private fun toggleDetailVisibility(isVisible: Boolean) {
    if (isVisible) {
      binding.detailInfoLayout.visibility = View.VISIBLE
      binding.timePickerContainer.visibility = View.GONE
    } else {
      binding.detailInfoLayout.visibility = View.GONE
      binding.timePickerContainer.visibility = View.VISIBLE
    }
  }

  // 현재 시간을 "HH:mm" 형식으로 반환하는 메소드
  private fun getCurrentTime(): String {
    val calendar = Calendar.getInstance()
    val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return simpleDateFormat.format(calendar.time)
  }

  // 출발역과 도착역 정보를 기반으로 소요 시간을 가져오는 메소드
  private fun getTravelTime(stationStart: CategoryDTO, stationEnd: CategoryDTO) {
    val api = AppServerClass.instance

// 서버에서 이동 시간 소요를 요청
    api.getDistance(stationStart.scode.toString(), stationEnd.scode.toString())
      .enqueue(object : Callback<Int> {
        // onResponse 시 서버 응답을 처리하는 메소드
        override fun onResponse(call: Call<Int>, response: Response<Int>) {
          if (response.isSuccessful) {
            // 서버에서 받은 소요 시간 (분 단위)
            val travelTime = response.body()

            // 소요 시간이 정상적으로 반환되었을 때
            travelTime?.let {
              // travelTimes에 travelTime 값을 Int로 할당
              val travelTimes: Int = it
              binding.useTime.text = "$travelTimes 분 소요"
            }
          } else {
            // 실패 시 로깅
            Log.e("csy", "API 호출 실패: ${response.code()}")
            Log.d(
              "csy",
              "Sending request with departure: ${stationStart.scode}, arrival: ${stationEnd.scode}"
            )
            Toast.makeText(this@DetailActivity, "소요 시간 가져오기 실패", Toast.LENGTH_SHORT)
              .show()
          }
        }


        override fun onFailure(call: Call<Int>, t: Throwable) {
          Log.e("csy", "API 호출 실패: ${t.message}")
          Toast.makeText(this@DetailActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
        }
      })
  }

  // 경유 갯수를 가져오는 메소드
  private fun getExchangeCount(stationStart: CategoryDTO, stationEnd: CategoryDTO) {
    val api = AppServerClass.instance

    // 서버에서 경유 갯수를 요청
    api.getExchange(stationStart.scode.toString(), stationEnd.scode.toString())
      .enqueue(object : Callback<Int> {
        override fun onResponse(call: Call<Int>, response: Response<Int>) {
          if (response.isSuccessful) {
            val exchangeCount = response.body()

            exchangeCount?.let {
              // 경유 갯수를 화면에 표시
              binding.stationNumber.text = "$it 개역 경유"
            }
          } else {
            Log.d("csy", "API 호출 실패: ${response.code()}")
            Toast.makeText(this@DetailActivity, "경유 갯수 가져오기 실패", Toast.LENGTH_SHORT)
              .show()
          }
        }

        override fun onFailure(call: Call<Int>, t: Throwable) {
          Log.e("csy", "API 호출 실패: ${t.message}")
          Toast.makeText(this@DetailActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
        }
      })
  }


  var stScode = ""
  var edScode = ""


  // **변경된 부분** 추가
  private fun getClosestTrainTime(stationCode: String) {
    val currentTime = getCurrentTime().replace(":", "")  // 현재 시간

// API 호출
    AppServerClass.instance.getTrainTime(stScode, edScode, currentTime, "1")
      .enqueue(object : Callback<String> {
        override fun onResponse(call: Call<String>, response: Response<String>) {
          if (response.isSuccessful) {
            val trainTimeDifferences = response.body()

            // 로그로 전달된 값 확인
            Log.d(
              "TrainTimeActivity",
              "Received train time differences: $trainTimeDifferences"
            )

            // 가장 가까운 출발 시간 찾기
            trainTimeDifferences?.let {
              // trainTimeDifferences 값을 정수로 변환
              val trainTimeDiffInt = trainTimeDifferences.toString().replace(":", "").toInt()

              // btnOp3 버튼에 가장 가까운 출발 시간 표시
              binding.detailStart.text = trainTimeDifferences  // TextView에 값 설정
              Log.d(
                "TrainTimeActivity",
                "Closest departure time: $trainTimeDifferences"
              )
              Log.d(
                "TrainTimeActivity",
                "trainTimeDifferInt의 값: $trainTimeDiffInt"
              )

              // travelTimes 값을 가져와서 정수로 변환
              val travelTime = binding.useTime.text.toString().split(" ")[0].toIntOrNull() ?: 0

              // totalArrivalTime을 계산 (trainTimeDiffInt + travelTime)
              val totalArrivalTime = trainTimeDiffInt + travelTime
              Log.d("TrainTimeActivity", "totalArrivalTime의 값: $totalArrivalTime")

              var finaltime = totalArrivalTime.toString()
              Log.d("TrainTimeActivity", "finaltime의 값: $finaltime")

              // travelTimeString이 4자리 수로 올바르게 들어온다고 가정
              if (finaltime.length == 3) {
                finaltime = finaltime.padStart(4, '0'); // 4자리로 맞추고, 부족한 자리는 '0'으로 채움


                // 시간과 분을 각각 추출
                val hours = finaltime.substring(0, 2).toInt() // 첫 2자리는 시간
                val minutes = finaltime.substring(2, 4).toInt() // 나머지 2자리는 분
                // hh:mm 형식으로 포맷
                val formattedArrivalTime = String.format("%02d:%02d", hours, minutes)
                Log.d("TrainTimeActivity", "formattedArrivalTime의 값: $formattedArrivalTime")

                // formattedArrivalTime을 arrivalTime으로 표시
                binding.detailArrival.text = formattedArrivalTime
              } else if (finaltime.length == 4) {
                if (finaltime[2] == '6') {
                  // 2번째 자리수에 1을 더하고, 3번째 자리를 0으로 변경
                  finaltime = finaltime.substring(0, 1) + (finaltime[1].toString()
                    .toInt() + 1) + '0' + finaltime[3]
                } else {

                  // 시간과 분을 각각 추출
                  val hours = finaltime.substring(0, 2).toInt() // 첫 2자리는 시간
                  val minutes = finaltime.substring(2, 4).toInt() // 나머지 2자리는 분
                  // hh:mm 형식으로 포맷
                  val formattedArrivalTime = String.format("%02d:%02d", hours, minutes)
                  Log.d("TrainTimeActivity", "formattedArrivalTime의 값: $formattedArrivalTime")

                  // formattedArrivalTime을 arrivalTime으로 표시
                  binding.detailArrival.text = formattedArrivalTime
                }
              }
            }
          }
        }



              override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("TrainTimeActivity", "네트워크 오류: ${t.message}")
              }
            })  // enqueue 메서드 닫는 괄호
          }



          private fun getUpdatedTrainTime() {
            // currentTime을 업데이트된 offset에 맞게 계산
            val updatedTime = getUpdatedTime(timeOffset).replace(":", "")

            // API 호출
            AppServerClass.instance.getTrainTime(stScode, edScode, updatedTime, "1")
              .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                  if (response.isSuccessful) {
                    val trainTimeDifferences = response.body()

                    // 로그로 전달된 값 확인
                    Log.d(
                      "TrainTimeActivity",
                      "Received train time differences: $trainTimeDifferences"
                    )

                    // 가장 가까운 출발 시간 찾기
                    trainTimeDifferences?.let {
                      // trainTimeDifferences 값을 정수로 변환
                      val trainTimeDiffInt =
                        trainTimeDifferences.toString().replace(":", "").toInt()

                      // btnOp3 버튼에 가장 가까운 출발 시간 표시
                      binding.detailStart.text = trainTimeDifferences  // TextView에 값 설정
                      Log.d("TrainTimeActivity", "Closest departure time: $trainTimeDifferences")
                      Log.d("TrainTimeActivity", "trainTimeDifferInt의 값: $trainTimeDiffInt")

                      // travelTimes 값을 가져와서 정수로 변환
                      val travelTime =
                        binding.useTime.text.toString().split(" ")[0].toIntOrNull() ?: 0

                      // totalArrivalTime을 계산 (trainTimeDiffInt + travelTime)
                      val totalArrivalTime = trainTimeDiffInt + travelTime
                      Log.d("TrainTimeActivity", "totalArrivalTime의 값: $totalArrivalTime")

                      var finaltime = totalArrivalTime.toString()
                      Log.d("TrainTimeActivity", "finaltime의 값: $finaltime")

                      // travelTimeString이 4자리 수로 올바르게 들어온다고 가정
                      if (finaltime.length == 3) {
                        finaltime = finaltime.padStart(4, '0'); // 4자리로 맞추고, 부족한 자리는 '0'으로 채움
                        // 시간과 분을 각각 추출
                        val hours = finaltime.substring(0, 2).toInt() // 첫 2자리는 시간
                        val minutes = finaltime.substring(2, 4).toInt() // 나머지 2자리는 분
                        // hh:mm 형식으로 포맷
                        val formattedArrivalTime = String.format("%02d:%02d", hours, minutes)
                        Log.d("TrainTimeActivity", "formattedArrivalTime의 값: $formattedArrivalTime")

                        // formattedArrivalTime을 arrivalTime으로 표시
                        binding.detailArrival.text = formattedArrivalTime
                      } else if (finaltime.length == 4) {
                        // 시간과 분을 각각 추출
                        if (finaltime[2] == '6') {
                          // 2번째 자리수에 1을 더하고, 3번째 자리를 0으로 변경
                          finaltime = finaltime.substring(0, 1) + (finaltime[1].toString()
                            .toInt() + 1) + '0' + finaltime[3]
                        } else {
                          val hours = finaltime.substring(0, 2).toInt() // 첫 2자리는 시간
                          val minutes = finaltime.substring(2, 4).toInt() // 나머지 2자리는 분
                          // hh:mm 형식으로 포맷
                          val formattedArrivalTime = String.format("%02d:%02d", hours, minutes)
                          Log.d(
                            "TrainTimeActivity",
                            "formattedArrivalTime의 값: $formattedArrivalTime"
                          )
                          // formattedArrivalTime을 arrivalTime으로 표시
                          binding.detailArrival.text = formattedArrivalTime
                        }
                      }
                    }
                  }
                }

                  override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("TrainTimeActivity", "네트워크 오류: ${t.message}")
                  }
                })  // enqueue 메서드 닫는 괄호
              }

                  private fun getUpdatedTime(offset: Int): String {
                val calendar = Calendar.getInstance()
                val currentTime = getCurrentTime() // 현재 시간 가져오기

                // "HH:mm" 형식으로 currentTime을 파싱
                val currentCalendar = Calendar.getInstance()
                val currentHour = currentTime.substring(0, 2).toInt()
                val currentMinute = currentTime.substring(3, 5).toInt()

                currentCalendar.set(Calendar.HOUR_OF_DAY, currentHour)
                currentCalendar.set(Calendar.MINUTE, currentMinute)

                // 오프셋 적용 (누적된 timeOffset 값)
                currentCalendar.add(Calendar.MINUTE, offset)

                // "HH:mm" 형식으로 반환
                val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                return simpleDateFormat.format(currentCalendar.time)
              }


          }


