package com.example.icontest2.seller_food

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

interface SellerFoodCallAPI {
    @GET("/food-list/detail/{sellerId}")
    suspend fun sellerFoodCall(@Path("sellerId") sellerId: String): ResponseBody
}