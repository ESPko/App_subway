package com.example.app.dto

import com.google.gson.annotations.SerializedName

data class CategoryDTO (

  @SerializedName("name")
  var name: String,

  @SerializedName("scode")
  var scode: String,

  @SerializedName("line")
  var line: String
)