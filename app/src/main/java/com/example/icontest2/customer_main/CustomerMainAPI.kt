package com.example.icontest2.customer_main

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface CustomerMainAPI {
    @Multipart
    @POST("/customer/imgUpload/{customerId}/")
    suspend fun uploadImage(
        @Path("customerId") customerId: String,
        @Part images: MultipartBody.Part?
    ): ResponseBody
}