package com.example.icontest2.seller_food

import com.google.gson.annotations.SerializedName

data class SellerFoodDTO(
    @SerializedName("foodName") val foodName: String,
    @SerializedName("price") val price: Int,
    @SerializedName("foodSeq") val foodSeq: Int,
    @SerializedName("base64Img") val base64Img: String?
)
