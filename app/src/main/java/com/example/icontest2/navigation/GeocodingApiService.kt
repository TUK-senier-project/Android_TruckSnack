package com.example.icontest2.navigation

import com.mapbox.api.geocoding.v5.models.GeocodingResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GeocodingApiService {
    @GET("geocoding/v5/mapbox.places/{query}.json")
    suspend fun getGeocoding(
        @Path("query") query: String,
        @Query("types") types: String = "address",
        @Query("access_token") accessToken: String
    ): GeocodingResponse
}
