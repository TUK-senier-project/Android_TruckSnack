package com.example.icontest2

import com.google.firebase.database.core.Repo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Retrofit2Interface {
    @POST("customer/register")
    fun createUser(@Body user: DataClass): Call<ApiResponse>
}