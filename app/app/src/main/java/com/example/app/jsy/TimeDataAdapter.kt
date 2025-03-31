package com.example.app.jsy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.app.dto.TimeData

class TimeDataAdapter(
  private val context: Context,
  private val data: List<TimeData>  // TimeData는 API로 받은 데이터
) : BaseAdapter() {

  override fun getCount(): Int {
    return data.size
  }

  override fun getItem(position: Int): Any {
    return data[position]
  }

  override fun getItemId(position: Int): Long {
    return position.toLong()
  }

  override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
    // ViewHolder 패턴을 사용하여 성능을 최적화
    val view: View = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false)

    val dayTextView: TextView = view.findViewById(android.R.id.text1)
    val holiTextView: TextView = view.findViewById(android.R.id.text2)

    // 데이터 세팅
    val timeData = getItem(position) as TimeData
    dayTextView.text = "${timeData.day}"
//    holiTextView.text = "Holi: ${timeData.holi}"

    return view
  }
}
