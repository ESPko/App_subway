package com.example.app.dto

import com.google.gson.annotations.SerializedName

//  서버로 전달할 데이터 클래스
data class UserDTO(
//  서버에 전달 시 컨트롤러의 파라미터 이름과 다를 수 있으므로 이름을 맞추기 위해서 @SerializedName 사용
  @SerializedName("userId")
  var userId: String,

  @SerializedName("userPwd")
  var userPwd: String,

  @SerializedName("userNickName")
  var userNickName: String,

  @SerializedName("userEmail")
  var userEmail: String)














