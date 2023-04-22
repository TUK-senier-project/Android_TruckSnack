package com.example.icontest2.customer_food_list

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class CustomerFoodListDTO(
    @SerializedName("customerFoodListDTO") val customerFoodListDTO: List<CustomerFoodListDTOItem>
)
data class FoodListRankDTO(
    @SerializedName("location") val location: String,
    @SerializedName("businessName") val businessName: String,
    @SerializedName("deadline") val deadline: Int,
    @SerializedName("grade") val grade: Double,
    @SerializedName("id") val id: String,
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("sellerImgS3Url") val sellerImgS3Url: String?
)
data class CustomerFoodListDTOItem(
    @SerializedName("location") val location: String,
    @SerializedName("businessName") val businessName: String,
    @SerializedName("deadline") val deadline: Int,
    @SerializedName("grade") val grade: Double,
    @SerializedName("id") val id: String?,
    @SerializedName("sellerId") val sellerId: String?,
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("base64Img") val base64Img: String?,
    @SerializedName("sellerImgS3Url") val sellerImgS3Url: String?
) : Parcelable {

    // Parcelable.Creator 구현
    companion object CREATOR : Parcelable.Creator<CustomerFoodListDTOItem> {
        override fun createFromParcel(parcel: Parcel): CustomerFoodListDTOItem {
            return CustomerFoodListDTOItem(parcel)
        }

        override fun newArray(size: Int): Array<CustomerFoodListDTOItem?> {
            return arrayOfNulls(size)
        }
    }

    // Parcelable 구현
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(location)
        parcel.writeString(businessName)
        parcel.writeInt(deadline)
        parcel.writeDouble(grade)
        parcel.writeString(id)
        parcel.writeString(sellerId)
        parcel.writeString(phoneNumber)
        parcel.writeString(base64Img)
        parcel.writeString(sellerImgS3Url)
    }

    override fun describeContents(): Int {
        return 0
    }
}