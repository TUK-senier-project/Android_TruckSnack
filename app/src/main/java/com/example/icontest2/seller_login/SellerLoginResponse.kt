package com.example.icontest2.seller_login

import com.google.gson.annotations.SerializedName

data class SellerLoginResponse(
    @SerializedName("seller") val seller: Seller,
    @SerializedName("base64EncodedImage") val base64EncodedImage: String
)
data class Seller(
    @SerializedName("businessName") val businessName: String,
    @SerializedName("category") val category: Int,
    @SerializedName("content") val content: Any?,
    @SerializedName("deadline") val deadline: Int,
    @SerializedName("deleted") val deleted: Boolean,
    @SerializedName("grade") val grade: Double,
    @SerializedName("id") val id: String,
    @SerializedName("isCreated") val isCreated: Any?,
    @SerializedName("isUpdated") val isUpdated: Any?,
    @SerializedName("location") val location: Any?,
    @SerializedName("password") val password: Any?,
    @SerializedName("phoneNumber") val phoneNumber: Any?,
    @SerializedName("sellerImgS3Url") val sellerImgS3Url: Any?,
    @SerializedName("seq") val seq: Any?,
    @SerializedName("setSeq") val setSeq: Int
)
