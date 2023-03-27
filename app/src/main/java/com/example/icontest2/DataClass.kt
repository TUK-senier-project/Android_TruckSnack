package com.example.icontest2

import com.google.gson.annotations.SerializedName

data class DataClass(
    @SerializedName("id") val id: String,
    @SerializedName("password") val password: String,
    @SerializedName("name") val name: String,
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("location") val location: String,



)
