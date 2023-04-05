package com.example.icontest2.seller_login

import com.google.gson.annotations.SerializedName

data class SellerIdFindDTO(
    @SerializedName("businessName") val businessName: String,
    @SerializedName("phoneNumber") val phoneNumber: String
)
