package com.example.app.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class CategoryDTO (

  @SerializedName("name")
  var name: String,

  @SerializedName("scode")
  var scode: String,

  @SerializedName("line")
  var line: String
) : Parcelable {
  constructor(parcel: Parcel) : this(
    parcel.readString() ?: "",
    parcel.readString() ?: "",
    parcel.readString() ?: ""
  )

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(name)
    parcel.writeString(scode)
    parcel.writeString(line)
  }

  override fun describeContents(): Int = 0

  companion object {
    @JvmField
    val CREATOR: Parcelable.Creator<CategoryDTO> = object : Parcelable.Creator<CategoryDTO> {
      override fun createFromParcel(parcel: Parcel): CategoryDTO {
        return CategoryDTO(parcel)
      }

      override fun newArray(size: Int): Array<CategoryDTO?> {
        return arrayOfNulls(size)
      }
    }
  }
}
