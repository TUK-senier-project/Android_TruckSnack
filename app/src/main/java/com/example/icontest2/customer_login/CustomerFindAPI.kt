package com.example.icontest2.customer_login


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CustomerFindAPI {
    // 보내는 restapi 형식 확인 post인지 get인지, URL확인
    // 파인드를 하면 아이디를 주는데 유닛(돌려받을게 없단뜻)
    @POST("/customer/idfind")
    suspend fun findCustomer(@Body customerData: CustomerFindDTO): Response<String> // 이렇게 하거나
    //suspend fun findCustomer(@Body customerData: CustomerFindDTO): String // 이렇게 하거나
}
