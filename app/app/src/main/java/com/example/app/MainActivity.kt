package com.example.app

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app.databinding.ActivityMainBinding
import com.example.app.dto.UserDTO
import com.example.app.retrofit.AppServerClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

  private val binding: ActivityMainBinding by lazy {
    ActivityMainBinding.inflate(layoutInflater)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContentView(binding.root)
    ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
      insets

    }

    binding.toolbar.findViewById<LinearLayout>(R.id.search).setOnClickListener {
      Log.d("fullstack503", "역 검색")
      Toast.makeText(this, "역검색 클릭", Toast.LENGTH_SHORT).show()
    }

    initEventListener()
  }

  private fun initEventListener() {

//    기본 GET 방식 통신, 파라미터 없음
    binding.btnGet1.setOnClickListener {
      Log.d("csy", "gettest1 시작")
      val api = AppServerClass.instance
      val call = api.getTest1()

//      Retrofit 통신 응답 부분, 따로 메소드로 만들어도 됨
//      Callback<String> 부분이 서버에서 전달받을 데이터 타입임
      call.enqueue(object : Callback<String>{
        override fun onResponse(p0: Call<String>, res: Response<String>) {
          if (res.isSuccessful) {
//            서버에서 전달받은 데이터만 변수로 저장
            val result = res.body()
            Log.d("csy", "result : $result")
          }
          else {
            Log.d("csy", "송신 실패, 응답 코드: ${res.code()} 메시지: ${res.message()}")
          }
        }

        override fun onFailure(p0: Call<String>, t: Throwable) {
          Log.d("csy", "message : $t.message")
        }
      })
    }

    binding.btnGet2.setOnClickListener {
      Log.d("csy", "gettest2 시작")
      val api = AppServerClass.instance
      val call = api.getTest2("매개변수2")
      retrofitResponse(call)
    }

    binding.btnGet3.setOnClickListener {
      Log.d("csy", "gettest3 시작")
      val api = AppServerClass.instance
      val call = api.getTest3(param1 = "path 방식 파라미터1", param2 = "path 방식 파라미터2")
      retrofitResponse(call)
    }

    binding.btnPost1.setOnClickListener {
      Log.d("csy", "posttest1 시작")
      val api = AppServerClass.instance
      val call = api.postTest1()
      retrofitResponse(call)
    }

    binding.btnPost2.setOnClickListener {
      Log.d("csy", "posttest2 시작")

//      DTO 타입 객체 생성
      val user: UserDTO = UserDTO(
        "test1",
        "1234",
        "테스터1",
        "test1@bitc.ac.kr"
      )

      val api = AppServerClass.instance
//      DTO 타입을 서버로 전달
      val call = api.postTest2(user)
      retrofitResponse(call)
    }

    binding.btnPut1.setOnClickListener {
      Log.d("csy", "puttest1 시작")
      val api = AppServerClass.instance
      val call = api.putTest1()
      retrofitResponse(call)
    }

    binding.btnPut2.setOnClickListener {
      Log.d("csy", "puttest2 시작")

      val user: UserDTO = UserDTO(
        "test1",
        "1234",
        "테스터1",
        "test1@bitc.ac.kr"
      )

      val api = AppServerClass.instance
//      DTO 타입과 일반 데이터를 함께 전달
      val call = api.putTest2(user, param1 = "매개변수 1")
      retrofitResponse(call)
    }

    binding.btnDelete.setOnClickListener {
      Log.d("csy", "deletetest1 시작")
      val api = AppServerClass.instance
      val call = api.deleteTest1(param1 = "매개변수 1")
      retrofitResponse(call)
    }
    binding.btnGet5.setOnClickListener {
      Log.d("csy", "gettest5 시작")
      val api = AppServerClass.instance
      val call = api.getTest5()
      retrofitResponse(call)
    }

  }


  //  Retrofit 통신 응답 부분을 따로 메소드로 분리
  private fun retrofitResponse(call: Call<String>) {

    call.enqueue(object : Callback<String>{
      override fun onResponse(p0: Call<String>, res: Response<String>) {
        if (res.isSuccessful) {
          val result = res.body()
          Log.d("csy", "result : $result")
        }
        else {
          Log.d("csy", "송신 실패, 응답 코드: ${res.code()} 메시지: ${res.message()}")
        }
      }

      override fun onFailure(p0: Call<String>, t: Throwable) {
        Log.d("csy", "message : $t.message")
      }
    })
  }
}