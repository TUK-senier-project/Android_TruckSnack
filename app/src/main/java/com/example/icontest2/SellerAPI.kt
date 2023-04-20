package com.example.icontest2

import com.example.icontest2.seller_login.SellerIdFindDTO
import com.example.icontest2.seller_login.SellerLoginDTO
import com.example.icontest2.seller_register.SellerCheckIdDTO
import com.example.icontest2.seller_register.SellerRegisterDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import okhttp3.ResponseBody

interface SellerAPI {
    @POST("/seller/register")
    suspend fun registerSeller(@Body sellerData: SellerRegisterDTO): Response<Unit>
    @POST("/seller/register-check")
    suspend fun registerSellerIdCheck(@Body sellerData: SellerCheckIdDTO): String
    @POST("/seller/idfind")
    suspend fun sellerIdFind(@Body sellerData: SellerIdFindDTO): String
    @POST("/seller/login")
    suspend fun sellerLogin(@Body sellerData: SellerLoginDTO): ResponseBody
}