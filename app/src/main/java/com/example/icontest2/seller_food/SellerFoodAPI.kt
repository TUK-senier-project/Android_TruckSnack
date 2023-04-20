package com.example.icontest2.seller_food

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*



interface SellerFoodAPI {
    @Multipart
    @POST("/food/{sellerId}/upload-food")
    //@Headers("Content-Type: multipart/form-data")
    suspend fun sellerFood(
        @Path("sellerId") sellerId: String,
        @Part("foodName") foodName: RequestBody,
        @Part("price") price: RequestBody,
        @Part images: MultipartBody.Part
    ): ResponseBody
}


