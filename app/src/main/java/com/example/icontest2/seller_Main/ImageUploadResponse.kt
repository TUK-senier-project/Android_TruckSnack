package com.example.icontest2.seller_Main

import com.google.gson.annotations.SerializedName

/*data class ImageUploadResponse(
    val success: Boolean,
    val data: String
)*/
data class UploadImageResponse(
    @SerializedName("uploadedImageUri") val uploadedImageUri: String
)
data class ImageResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("url")
    val url: String
)