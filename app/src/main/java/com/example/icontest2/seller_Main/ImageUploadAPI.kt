package com.example.icontest2.seller_Main


import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

/*interface ImageUploadAPI {
    @Multipart
    @POST("/seller/imgUpload/{sellerId}/")
    fun uploadImage(@Part image: MultipartBody.Part): Call<ImageUploadResponse>
}*/
/*interface ApiService {
    @Multipart
    @POST("/seller/imgUpload/{sellerId}/")
    fun uploadImage(@Part image: MultipartBody.Part): Call<UploadImageResponse>
}*/
/*interface ApiService {
    @Multipart
    @POST("/seller/imgUpload/{sellerId}/")
    fun uploadImage(@Part("sellerId") sellerId: String, @Part images: MultipartBody.Part): Call<UploadImageResponse>
}*/
interface ImageUploadAPI{
    @Multipart
    @POST("seller/imgUpload/{sellerId}/")
    suspend fun uploadImage(
        @Path("sellerId") sellerId: String,
        @Part images: MultipartBody.Part?
    ): ResponseBody
}

