package com.example.icontest2.test

import com.example.icontest2.SellerDTO
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

interface TestAPI {
    @POST("/seller/register")
    suspend fun registerSeller(@Body sellerData: SellerDTO): ResponseBody
}
