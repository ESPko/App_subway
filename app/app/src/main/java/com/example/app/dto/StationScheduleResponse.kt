package com.example.app.dto

import com.google.gson.annotations.SerializedName

// Timeup, Timedown의 공통 부분을 하나의 클래스로 정의
data class TimeData(
  @SerializedName("upIdx") val upIdx: Int?,
  @SerializedName("downIdx") val downIdx: Int?,
  @SerializedName("scode") val scode: Int,
  @SerializedName("day") val day: String,
  @SerializedName("sat") val sat: String,
  @SerializedName("holi") val holi: String
)

// API 응답을 나타내는 클래스
data class StationScheduleResponse(
  @SerializedName("timeups") val timeups: List<TimeData>,
  @SerializedName("timedowns") val timedowns: List<TimeData>
)