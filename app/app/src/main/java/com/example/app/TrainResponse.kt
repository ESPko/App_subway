package com.example.app

import com.google.gson.annotations.SerializedName

data class TrainResponse (

  @SerializedName("노포") var 노포: List<Int>,

  @SerializedName("다대포해수욕장") var 다대포해수욕장: List<Int>,

  @SerializedName("양산") var 양산: List<Int>,

  @SerializedName("장산") var 장산: List<Int>



)