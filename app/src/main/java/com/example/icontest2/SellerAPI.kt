package com.example.icontest2

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SellerAPI {
    @POST("/seller/register")
    suspend fun registerSeller(@Body sellerData: SellerDTO): Response<Unit>
}