package com.example.icontest2.seller_login

import android.os.Parcel
import android.os.Parcelable
import com.example.icontest2.customer_food_list.CustomerFoodDetailDTO
import com.google.gson.annotations.SerializedName

data class SellerLoginResponse(
    @SerializedName("seller") val seller: Seller,
    @SerializedName("base64EncodedImage") val base64EncodedImage: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable<Seller>(Seller::class.java.classLoader)!!,
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(seller, flags)
        parcel.writeString(base64EncodedImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SellerLoginResponse> {
        override fun createFromParcel(parcel: Parcel): SellerLoginResponse {
            return SellerLoginResponse(parcel)
        }

        override fun newArray(size: Int): Array<SellerLoginResponse?> {
            return arrayOfNulls(size)
        }
    }
}

data class Seller(
    @SerializedName("businessName") val businessName: String,
    @SerializedName("category") val category: Int,
    @SerializedName("content") val content: String?,
    @SerializedName("deadline") val deadline: Int,
    @SerializedName("deleted") val deleted: Boolean,
    @SerializedName("grade") val grade: Double,
    @SerializedName("id") val id: String,
    @SerializedName("isCreated") val isCreated: String?,
    @SerializedName("isUpdated") val isUpdated: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("password") val password: String?,
    @SerializedName("phoneNumber") val phoneNumber: String?,
    @SerializedName("sellerImgS3Url") val sellerImgS3Url: String?,
    @SerializedName("seq") val seq: Long?,
    @SerializedName("setSeq") val setSeq: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(businessName)
        parcel.writeInt(category)
        parcel.writeString(content)
        parcel.writeInt(deadline)
        parcel.writeByte(if (deleted) 1 else 0)
        parcel.writeDouble(grade)
        parcel.writeString(id)
        parcel.writeString(isCreated)
        parcel.writeString(isUpdated)
        parcel.writeString(location)
        parcel.writeString(password)
        parcel.writeString(phoneNumber)
        parcel.writeString(sellerImgS3Url)
        parcel.writeLong(seq ?: -1)
        parcel.writeInt(setSeq)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Seller> {
        override fun createFromParcel(parcel: Parcel): Seller {
            return Seller(parcel)
        }

        override fun newArray(size: Int): Array<Seller?> {
            return arrayOfNulls(size)
        }
    }
}