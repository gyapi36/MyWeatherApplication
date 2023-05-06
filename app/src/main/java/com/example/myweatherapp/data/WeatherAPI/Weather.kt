package com.example.myweatherapp.data.WeatherAPI


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Weather(
    @SerialName("description")
    val description: String? = null,
    @SerialName("icon")
    val icon: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("main")
    val main: String? = null
)