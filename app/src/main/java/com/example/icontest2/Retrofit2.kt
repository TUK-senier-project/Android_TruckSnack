package com.example.icontest2

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofit2 {
    private const val URL = "http://13.209.9.240:8080"

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

   // val service: RetrofitService = retrofit.create(RetrofitService::class.java)
}