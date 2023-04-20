package com.example.icontest2.seller_order

import com.google.gson.annotations.SerializedName

data class SellerOrderManagementDTO(
    @SerializedName("customerId") val customerId: String,
    @SerializedName("isCreated") val isCreated: List<Int>,
    @SerializedName("orderState") val orderState: Int,
    @SerializedName("orderTotalPrice") val orderTotalPrice: Int,
    @SerializedName("sellerId") val sellerId: String,
    @SerializedName("seq") val seq: Int
)
data class SellerIdDTO(
    @SerializedName("sellerId") val sellerId: String
)
data class SellerOrderDetailDTO(
    @SerializedName("seq") val seq: Int
)
data class SellerOrderDetailResponse(
    @SerializedName("foodSeq") val foodSeq: Int,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("totalPrice") val totalPrice: Int
)