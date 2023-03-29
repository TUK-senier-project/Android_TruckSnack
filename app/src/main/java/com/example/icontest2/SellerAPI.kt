package com.example.icontest2

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SellerAPI {
    @POST("/seller/register")
    fun registerSeller(@Body sellerData: SellerDTO): Call<Void>
}
