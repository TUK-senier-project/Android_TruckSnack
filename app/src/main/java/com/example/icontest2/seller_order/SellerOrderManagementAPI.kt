package com.example.icontest2.seller_order

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SellerOrderManagementAPI {
    @POST("/orderPayment/order-list")
    suspend fun sellerOrderList(@Body sellerId: SellerIdDTO): ResponseBody
    @POST("/orderPayment/detail-order-list/")
    suspend fun sellerOrderDetailList(@Body seq: SellerOrderDetailDTO): ResponseBody
    @POST("/orderPayment/order_cancel")
    suspend fun sellerOrderCancel(@Body seq: SellerOrderDetailDTO): ResponseBody
    @POST("/orderPayment/order_check")
    suspend fun sellerOrderCheck(@Body seq: SellerOrderDetailDTO): ResponseBody
    @POST("/orderPayment/order_complete")
    suspend fun sellerOrderComplete(@Body seq: SellerOrderDetailDTO): ResponseBody
}