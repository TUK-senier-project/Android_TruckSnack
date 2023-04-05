package com.example.icontest2.seller_register

import com.google.gson.annotations.SerializedName

data class SellerRegisterDTO(
    @SerializedName("id") val id: String,
    @SerializedName("password") val password: String,
    @SerializedName("businessName") val businessName: String,
    @SerializedName("content") val content: String,
    @SerializedName("category") val category: Int,
    @SerializedName("deadline") val deadline: Int,
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("location") val location: String,
)
