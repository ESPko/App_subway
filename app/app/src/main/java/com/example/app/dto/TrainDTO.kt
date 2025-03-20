package com.example.app.dto

import com.google.gson.annotations.SerializedName

data class TrainDTO (

  @SerializedName("endSn")
  var endSn: String,

  @SerializedName("endSc")
  var endSc: String

)