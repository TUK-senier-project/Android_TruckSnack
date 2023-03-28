package com.example.icontest2

import com.google.gson.annotations.SerializedName

data class SellerResponseBody(
    @SerializedName("result")
    val result: String?,
    @SerializedName("status")
    val status: String?
)
