package com.example.app.min


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.dto.CategoryDTO
import com.example.app.databinding.ItemStationTestBinding
class SubSearchAdapterTest (private var stationSearches: List<CategoryDTO>) : RecyclerView.Adapter<SubSearchAdapterTest.StationViewHolder>() {

    private var allStationSearches: List<CategoryDTO> = stationSearches

    //  ViewHolder 클래스: 각 역 항목(ItemStationBinding)을 화면에 바인딩하는 역할을 수행
    class StationViewHolder(val binding: ItemStationTestBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(stationSearch: CategoryDTO) {

//      역 이름
            binding.stationName.text = stationSearch.name

            val lineImageRes = when (stationSearch.line) {
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
        val binding = ItemStationTestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StationViewHolder(binding)
    }

    //  ViewHolder와 데이터를 연결하는 함수
    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        val station = stationSearches[position]
        holder.bind(station)
    }
    //  데이터 개수를 반환
    override fun getItemCount() = stationSearches.size

    // RecyclerView 데이터 업데이트 함수
    fun updateData(newStationSearches: List<CategoryDTO>) {
        this.allStationSearches = newStationSearches
        this.stationSearches = newStationSearches
        notifyDataSetChanged()
    }

    // 전체 역 데이터를 반환하는 함수 (필터링용)
    fun getAllStations(): List<CategoryDTO>{
        return allStationSearches
    }
}