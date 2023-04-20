package com.example.icontest2.customer_register

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("result") val result: String,
)
