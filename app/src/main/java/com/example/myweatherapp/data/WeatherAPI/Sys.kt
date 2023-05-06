package com.example.myweatherapp.data.WeatherAPI


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Sys(
    @SerialName("country")
    val country: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("sunrise")
    val sunrise: Int? = null,
    @SerialName("sunset")
    val sunset: Int? = null,
    @SerialName("type")
    val type: Int? = null
)