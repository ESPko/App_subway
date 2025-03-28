import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app.MainActivity
import com.example.app.R
import com.example.app.databinding.ItemStationBinding
import com.example.app.dto.CategoryDTO
import com.example.app.jsy.StationDetailActivity

class SubSearchAdapter(private var categories: List<CategoryDTO>) : RecyclerView.Adapter<SubSearchAdapter.StationViewHolder>() {

  // ViewHolder 클래스: 각 역 항목(ItemStationBinding)을 화면에 바인딩하는 역할을 수행
  class StationViewHolder(val binding: ItemStationBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(stationSearch: CategoryDTO) {

      // 역 이름class SubSearchAdapter(private var categories: List<Station>) : RecyclerView.Adapter<SubSearchAdapter.StationViewHolder>() {
      //
      //    // ViewHolder 클래스: 각 역 항목(ItemStationBinding)을 화면에 바인딩하는 역할을 수행
      //    class StationViewHolder(val binding: ItemStationBinding) : RecyclerView.ViewHolder(binding.root) {
      //        fun bind(station: Station) {
      //
      //            // 역 이름
      //            binding.stationName.text = station.name
      //
      //            val lineImageRes = when (station.line) {
      //                1 -> R.drawable.line_1
      //                2 -> R.drawable.line_2
      //                3 -> R.drawable.line_3
      //                4 -> R.drawable.line_4
      //                else -> R.drawable.what // 기본 이미지
      //            }
      //            binding.lineImage.setImageResource(lineImageRes)
      //
      //            // 출발 버튼 클릭 리스너
      //            binding.startButton.setOnClickListener {
      //                val context = binding.root.context
      //                val intent = Intent(context, MainActivity::class.java)
      //                context.startActivity(intent)
      //                (context as Activity).finish() // 현재 액티비티 종료하고 MainActivity로 돌아가기
      //            }
      //
      //            // 역 정보 버튼 클릭 리스너
      //            binding.subInfoButton.setOnClickListener {
      //                val context = binding.root.context
      //
      //                // 클릭한 항목의 역 정보
      //                val station = station // bind 함수에서 전달받은 station 객체 사용
      //
      //                // StationDetailActivity로 데이터를 전달하는 Intent 생성
      //                val intent = Intent(context, StationDetailActivity::class.java)
      //
      //                // 데이터 전달
      //                intent.putExtra("scode", station.scode)  // 역 코드
      //                intent.putExtra("name", station.name)    // 역 이름
      //                intent.putExtra("line", station.line)    // 역 노선
      //
      //                // StationDetailActivity로 데이터 전달 후 새 액티비티 시작
      //                context.startActivity(intent)
      //            }
      //        }
      //    }
      //
      //    // 새로운 ViewHolder를 생성하는 함수
      //    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
      //        val binding = ItemStationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      //        return StationViewHolder(binding)
      //    }
      //
      //    // ViewHolder와 데이터를 연결하는 함수
      //    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
      //        val station = categories[position]  // 각 항목에 해당하는 Station 객체를 가져옴
      //        holder.bind(station)  // Station 객체를 bind 함수에 전달
      //    }
      //
      //    // 데이터 개수를 반환
      //    override fun getItemCount() = categories.size
      //
      //    // RecyclerView 데이터 업데이트 함수
      //    fun updateData(newCategories: List<Station>) {
      //        this.categories = newCategories
      //        notifyDataSetChanged()
      //    }
      //}
      binding.stationName.text = stationSearch.name

      val lineImageRes = when (stationSearch.line) {
        1 -> R.drawable.line_1
        2 -> R.drawable.line_2
        3 -> R.drawable.line_3
        4 -> R.drawable.line_4
        else -> R.drawable.what // 기본 이미지
      }
      binding.lineImage.setImageResource(lineImageRes)

      // 출발 버튼 클릭 리스너
      binding.startButton.setOnClickListener {
        val context = binding.root.context
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
        (context as Activity).finish() // 현재 액티비티 종료하고 MainActivity로 돌아가기
      }

      // 역 정보 버튼 클릭 리스너
      binding.subInfoButton.setOnClickListener {
        val context = binding.root.context

        // 클릭한 항목의 역 정보
        val station = stationSearch // bind 함수에서 전달받은 station 객체 사용

        // StationDetailActivity로 데이터를 전달하는 Intent 생성
        val intent = Intent(context, StationDetailActivity::class.java)

        // 데이터 전달
        intent.putExtra("scode", station.scode)  // 역 코드
        intent.putExtra("name", station.name)    // 역 이름
        intent.putExtra("line", station.line)    // 역 노선

        // StationDetailActivity로 데이터 전달 후 새 액티비티 시작
        context.startActivity(intent)
      }
    }
  }

  // 새로운 ViewHolder를 생성하는 함수
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
    val binding = ItemStationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return StationViewHolder(binding)
  }

  // ViewHolder와 데이터를 연결하는 함수
  override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
    val station = categories[position]  // 각 항목에 해당하는 Station 객체를 가져옴
    holder.bind(station)  // Station 객체를 bind 함수에 전달
  }

  // 데이터 개수를 반환
  override fun getItemCount() = categories.size

  // RecyclerView 데이터 업데이트 함수
  fun updateData(newCategories: List<CategoryDTO>) {
    this.categories = newCategories
    notifyDataSetChanged()
  }
}
