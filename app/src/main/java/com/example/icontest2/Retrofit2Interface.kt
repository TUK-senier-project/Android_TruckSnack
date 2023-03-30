package com.example.icontest2

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Retrofit2Interface {
    @POST("customer/register")
    fun createUser(@Body user: DataClass): Call<ApiResponse>
}