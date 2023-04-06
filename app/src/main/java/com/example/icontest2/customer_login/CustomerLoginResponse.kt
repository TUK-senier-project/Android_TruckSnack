package com.example.icontest2.customer_login

import com.google.gson.annotations.SerializedName

data class CustomerLoginResponse(
    @SerializedName("seq") val seq: Any,
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: Any,
    @SerializedName("password") val password: Any,
    @SerializedName("customerImgS3Url") val customerImgS3Url: Any,
    @SerializedName("phoneNumber") val phoneNumber: Any,
    @SerializedName("location") val location: Any,
    @SerializedName("isCreated") val isCreated: Any,
    @SerializedName("isUpdated") val isUpdated: Any,
    @SerializedName("deleted") val deleted: Boolean
)