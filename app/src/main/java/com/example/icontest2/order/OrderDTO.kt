package com.example.icontest2.order

import com.google.gson.annotations.SerializedName

data class OrderDTO(
    val foodSeq: Int,
    val quantity: Int
)
data class OrderListDTO(
    val foodSeq: Int,
    val foodName: String,
    val foodPrice: Int,
    var quantity: Int,
    var foodTotalPrice: Int
)
data class OrderResponse(
    @SerializedName("customerId") val customerId: String,
    @SerializedName("deleted") val deleted: Boolean,
    @SerializedName("isCreated") val isCreated: List<Int>,
    @SerializedName("isUpdated") val isUpdated: List<Int>,
    @SerializedName("orderState") val orderState: Int,
    @SerializedName("orderTotalPrice") val orderTotalPrice: Int,
    @SerializedName("sellerId") val sellerId: String,
    @SerializedName("seq") val seq: Int
)

