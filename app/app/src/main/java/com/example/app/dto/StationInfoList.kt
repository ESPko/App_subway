package com.example.app.dto

import com.google.gson.annotations.SerializedName

data class StationInfoList (

  @SerializedName("scode")
  val scode:  Int,

  @SerializedName("door")
  val door: String,

  @SerializedName("toilet")
  val toilet: String,

  @SerializedName("across")
  val across: String,

  @SerializedName("flatform")
  val flatform: String,

  @SerializedName("storage")
  val storage: String?,

  @SerializedName("util")
  val util: String,

  @SerializedName("address")
  val address: String,

  @SerializedName("number")
  val number: String

)