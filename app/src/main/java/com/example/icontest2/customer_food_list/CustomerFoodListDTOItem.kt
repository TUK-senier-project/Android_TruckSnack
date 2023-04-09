package com.example.icontest2.customer_food_list

import com.google.gson.annotations.SerializedName

data class CustomerFoodListDTOItem(
    @SerializedName("businessName") val businessName: String,
    @SerializedName("deadline") val deadline: Int,
    @SerializedName("grade") val grade: Double,
    @SerializedName("id") val id: String,
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("sellerImgS3Url") val sellerImgS3Url: Any
)