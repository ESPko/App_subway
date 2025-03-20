package com.example.app.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import kotlin.jvm.java

//  Retrofit 의 기본 설정 클래스
object AppServerClass {

  private val BASE_URL = "http://10.100.203.88:8080/app/"

  val instance: AppServerInterface by lazy {
    Retrofit.Builder()
//      서버 기본 주소
      .baseUrl(BASE_URL)
//      서버에서 전달받을 데이터가 일반 문자열일 경우 추가
      .addConverterFactory(ScalarsConverterFactory.create())
//      서버에서 전달받을 데이터가 JSON 타입일 경우 추가
      .addConverterFactory(GsonConverterFactory.create())
      .build()
      .create(AppServerInterface::class.java)
  }
}














