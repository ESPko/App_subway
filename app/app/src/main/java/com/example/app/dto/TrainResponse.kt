package com.example.app.dto

import com.google.gson.annotations.SerializedName

data class TrainResponse (

  @SerializedName("노포") var 노포: List<Int>,

  @SerializedName("다대포해수욕장") var 다대포해수욕장: List<Int>,

  @SerializedName("양산") var 양산: List<Int>,

  @SerializedName("장산") var 장산: List<Int>,


  @SerializedName("수영(3)") var 수영: List<Int>,

  @SerializedName("대저") var 대저: List<Int>,

  @SerializedName("미남(4)") var 미남: List<Int>,

  @SerializedName("안평") var 안평: List<Int>


)