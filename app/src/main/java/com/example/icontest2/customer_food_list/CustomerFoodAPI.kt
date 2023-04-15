package com.example.icontest2.customer_food_list

import com.example.icontest2.seller_login.SellerLoginDTO
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CustomerFoodAPI {
    @GET("/food-list/{categoryNumber}")
    suspend fun customerFoodList(@Path("categoryNumber") categoryNumber: Int): ResponseBody
    @GET("/food-list/detail/{sellerId}")
    suspend fun customerFoodDetail(@Path("sellerId") sellerId: String): ResponseBody
    @GET("/rank/category/{type}&{categoryNumber}/")
    suspend fun getRanking(
        @Path("type") type: String,
        @Path("categoryNumber") categoryNumber: Int
    ): ResponseBody
}