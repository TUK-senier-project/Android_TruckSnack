package com.example.icontest2.order

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface OrderAPI {
    @POST("/orderPayment/{customer_id}/")
    suspend fun orderRegister(
        @Path("customer_id") customerId: String,
        @Body orderPayments: List<OrderDTO>
    ): ResponseBody
}