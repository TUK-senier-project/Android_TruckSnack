package com.example.icontest2

import com.example.icontest2.SellerRegister.SellerCheckIdDTO
import com.example.icontest2.SellerRegister.SellerRegisterDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SellerAPI {
    @POST("/seller/register")
    suspend fun registerSeller(@Body sellerData: SellerRegisterDTO): Response<Unit>
    @POST("/seller/register-check")
    suspend fun registerSellerIdCheck(@Body sellerData: SellerCheckIdDTO): String
}