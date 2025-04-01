package com.example.app.retrofit
import com.example.app.dto.StationInfoList
import com.example.app.dto.TrainResponse
import com.example.app.dto.CategoryDTO
import com.example.app.dto.StationScheduleResponse
import com.example.app.dto.UserDTO
import com.example.app.jsy.Station
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

//  Retrofit 을 사용하여 서버와 통신 시 사용할 메소드 형식을 미리 설정
interface AppServerInterface {

  @GET("gettest1")
  fun getTest1(): Call<String>

  @GET("gettest2?param1=파라미터1")
  fun getTest2(@Query("param2") param2: String): Call<String>

  @GET("gettest3/{param1}/{param2}")
  fun getTest3(@Path("param1") param1: String, @Path("param2") param2: String): Call<String>

  @POST("posttest1")
  fun postTest1(): Call<String>

  @POST("posttest2")
  fun postTest2(@Body user: UserDTO): Call<String>

  @PUT("puttest1")
  fun putTest1(): Call<String>

  @PUT("puttest2")
  fun putTest2(@Body user: UserDTO, @Query("param1") param1: String): Call<String>

  @DELETE("deletetest1")
  fun deleteTest1(@Query("param1") param1: String): Call<String>



  @GET("app/category")

  fun getCategories(): Call<List<CategoryDTO>>  // Returns the list of CategoryDTO

  @GET("time/total/{stStation}/{edStation}")
  fun getDistance(
    @Path("stStation") stStation: String,
    @Path("edStation") edStation: String
  ): Call<Int>  // 소요 시간(분)을 반환

  // 추가: 경유 갯수 가져오기
  @GET("station/exchange/{stStation}/{edStation}")
  fun getExchange(@Path("stStation") stStation: String, @Path("edStation") edStation: String): Call<Int>

  @GET("app")
  fun getApi(): Call<String>


  @GET("app/{param1}/{param2}")
  fun getApi2(@Path("param1") param1: String, @Path("param2") param2: String): Call<String>

  @GET("app/stationListName/{scode}")
  fun getCategoryName(@Path("scode") scode: String): Call<List<Station>>

//  @GET("app/train/{scode}/{sttime}/{day}")
//  fun getTrainTimeAndName(@Path("scode") scode: String, @Path("sttime") sttime: String, @Path("day") day: String): Call<TrainResponse>

  @GET("app/train/{scode}/{sttime}/{day}")
  suspend fun getTrainTimeAndName(
    @Path("scode") scode: String,
    @Path("sttime") sttime: String,
    @Path("day") day: String
  ): TrainResponse

  @GET("app/station/{scode}")
  fun getStationInfo(@Path("scode") scode: String): Call<List<StationInfoList>>

  @GET("app/StationSheet/{scode}")
  fun getStationSheet(@Path("scode") scode: String): Call<StationScheduleResponse>

  @GET("app/trainTime/{stScode}/{edScode}/{sttime}/{day}")
  fun getTrainTime(
    @Path("stScode") stScode: String,
    @Path("edScode") edScode: String,
    @Path("sttime") sttime: String,
    @Path("day") day: String
  ): Call<String> // 서버 응답은 Map<String, List<Integer>> 형태

}














