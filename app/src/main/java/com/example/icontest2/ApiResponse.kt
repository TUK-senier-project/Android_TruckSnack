package com.example.icontest2

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("result") val result: String,
)
