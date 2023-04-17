package com.example.icontest2.customer_food_list

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class CustomerFoodDetailDTO(
    @SerializedName("foodName") val foodName: String,
    @SerializedName("price") val price: Int,
    @SerializedName("foodSeq") val foodSeq: Int,
    @SerializedName("base64Img") val base64Img: String?
) : Parcelable {
    // Parcelable.Creator 구현
    companion object CREATOR : Parcelable.Creator<CustomerFoodDetailDTO> {
        override fun createFromParcel(parcel: Parcel): CustomerFoodDetailDTO {
            return CustomerFoodDetailDTO(parcel)
        }

        override fun newArray(size: Int): Array<CustomerFoodDetailDTO?> {
            return arrayOfNulls(size)
        }
    }

    // Parcelable 구현
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(foodName)
        parcel.writeInt(price)
        parcel.writeInt(foodSeq)
        parcel.writeString(base64Img)
    }

    override fun describeContents(): Int {
        return 0
    }
}