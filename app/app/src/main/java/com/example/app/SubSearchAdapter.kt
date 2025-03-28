package com.example.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app.databinding.ItemStationBinding


//  SubSearchAdapter: 역 목록을 표시하는 RecyclerView 어댑터
class SubSearchAdapter(private var categories: List<Station>) : RecyclerView.Adapter<SubSearchAdapter.StationViewHolder>() {

  private var allCategories: List<Station> = categories

//  ViewHolder 클래스: 각 역 항목(ItemStationBinding)을 화면에 바인딩하는 역할을 수행
  class StationViewHolder(val binding: ItemStationBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(station: Station) {

//      역 이름
      binding.stationName.text = station.name

      val lineImageRes = when (station.line) {
        1 -> R.drawable.line_1
        2 -> R.drawable.line_2
        3 -> R.drawable.line_3
        4 -> R.drawable.line_4
        else -> R.drawable.what // 기본 이미지
      }
      binding.lineImage.setImageResource(lineImageRes)


      binding.startButton.setOnClickListener {
        // 출발 버튼 클릭 이벤트 처리
      }
      binding.subInfoButton.setOnClickListener {
        // 역 정보 버튼 클릭 이벤트 처리
      }
    }
  }
// 새로운 ViewHolder를 생성하는 함수
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
    val binding = ItemStationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return StationViewHolder(binding)
  }

//  ViewHolder와 데이터를 연결하는 함수
  override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
    val station = categories[position]
    holder.bind(station)
  }
//  데이터 개수를 반환
  override fun getItemCount() = categories.size

  // RecyclerView 데이터 업데이트 함수
  fun updateData(newCategories: List<Station>) {
    this.categories = newCategories
    this.allCategories = newCategories
    notifyDataSetChanged()
  }
}
