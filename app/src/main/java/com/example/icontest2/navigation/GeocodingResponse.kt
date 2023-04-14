package com.example.icontest2.navigation

data class GeocodingResponse(
    val features: List<Feature>
)

data class Feature(
    val center: List<Double>
)

