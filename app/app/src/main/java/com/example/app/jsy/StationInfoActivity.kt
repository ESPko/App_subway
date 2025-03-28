//package com.example.app.jsy
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import com.example.app.databinding.ActivityStationInfoBinding
//import com.example.app.retrofit.AppServerClass
//import org.json.JSONArray
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//class StationInfoActivity : AppCompatActivity() {
//
//  private val binding:ActivityStationInfoBinding by lazy {
//    ActivityStationInfoBinding.inflate(layoutInflater)
//  }
//
//  override fun onCreate(savedInstanceState: Bundle?) {
//    super.onCreate(savedInstanceState)
//    enableEdgeToEdge()
//    setContentView(binding.root)
//
//    ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
//      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//       insets
//    }
//
//    // intent에서 데이터 받아오기
//    val JsonString = intent.getStringExtra("data")
//
//    // result 값 전체
//    binding.stationText.setText("$JsonString")
//
//    JsonString?.let{
//      val jsonArray = JSONArray(it)
//
//      if(jsonArray.length() > 0){
//        val firstItem = jsonArray.getJSONObject(0)
//
////        binding.dist.setText(firstItem.getString("dist"))
//        binding.endSc.setText(firstItem.getString("endSc"))
//        binding.endSn.setText(firstItem.getString("endSn"))
//      }
//
//      binding.btnUpdate.setOnClickListener{
////        val endSc = binding.endSc.text
////        val api = AppServerClass.instance
////        val call = api.getCategoryName(param1 = "$endSc")
////        retrofitResponse(call)
//      }
//    }
//
//  }
//  private fun retrofitResponse(call: Call<String>) {
//
//    call.enqueue(object : Callback<String> {
//      override fun onResponse(p0: Call<String>, res: Response<String>) {
//        if (res.isSuccessful) {
//          val result = res.body()
//          Log.d("csy", "result : $result")
//
//          binding.serverdata.text = "$result"
//
//        }
//        else {
//          Log.d("csy", "송신 실패, 응답 코드: ${res.code()} 메시지: ${res.message()}")
//        }
//      }
//
//      override fun onFailure(p0: Call<String>, t: Throwable) {
//        Log.d("csy", "message : $t.message")
//      }
//    })
//  }
//}