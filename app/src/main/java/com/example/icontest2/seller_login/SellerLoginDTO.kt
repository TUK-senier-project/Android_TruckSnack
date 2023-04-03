package com.example.icontest2.seller_login

import com.google.gson.annotations.SerializedName

data class SellerLoginDTO(
    @SerializedName("id") val id: String,
    @SerializedName("password") val password: String
)
