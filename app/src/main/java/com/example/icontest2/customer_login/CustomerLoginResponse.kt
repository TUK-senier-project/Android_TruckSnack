package com.example.icontest2.customer_login

import com.google.gson.annotations.SerializedName

data class CustomerLoginResponse(
    val base64EncodedImage: String,
    val customer: Customer
)
data class Customer(
    @SerializedName("seq") val seq: Any,
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("password") val password: String,
    @SerializedName("customerImgS3Url") val customerImgS3Url: Any,
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("location") val location: String,
    @SerializedName("isCreated") val isCreated: Any,
    @SerializedName("isUpdated") val isUpdated: Any,
    @SerializedName("deleted") val deleted: Boolean
)