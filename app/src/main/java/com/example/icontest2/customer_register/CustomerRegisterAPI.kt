package com.example.icontest2.customer_register

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CustomerRegisterAPI {
    // 보내는 restapi 형식 확인 post인지 get인지, URL확인
    @POST("/customer/register")
    suspend fun registerCustomer(@Body customerData: CustomerRegisterDTO): Response<Unit>
    @POST("/customer/register-check")
    suspend fun registerCustomerIdCheck(@Body customerData: CustomerCheckIdDTO): Response<Unit>

    //@POST("/customer/register-check")
    //suspend fun registerCustomerIdCheck(@Body customerData: CustomerCheckIdDTO): String

}



