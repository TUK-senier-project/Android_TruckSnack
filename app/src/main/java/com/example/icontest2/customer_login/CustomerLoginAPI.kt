package com.example.icontest2.customer_login


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CustomerLoginAPI {
    // 보내는 restapi 형식 확인 post인지 get인지, URL확인
    @POST("/customer/login")
    suspend fun loginCustomer(@Body customerData: CustomerLoginDTO): Response<Unit>
}
