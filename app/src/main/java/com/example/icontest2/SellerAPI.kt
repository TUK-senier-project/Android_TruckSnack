package com.example.icontest2

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SellerAPI {
    @POST("/seller/register")
    fun sellerRegister(@Body sellerInfo: SellerDTO): Call<SellerResponseBody>
}
