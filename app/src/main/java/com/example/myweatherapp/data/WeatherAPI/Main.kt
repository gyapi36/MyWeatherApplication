package com.example.myweatherapp.data.WeatherAPI


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Main(
    @SerialName("feels_like")
    val feelsLike: Double? = null,
    @SerialName("grnd_level")
    val grndLevel: Int? = null,
    @SerialName("humidity")
    val humidity: Int? = null,
    @SerialName("pressure")
    val pressure: Int? = null,
    @SerialName("sea_level")
    val seaLevel: Int? = null,
    @SerialName("temp")
    val temp: Double? = null,
    @SerialName("temp_max")
    val tempMax: Double? = null,
    @SerialName("temp_min")
    val tempMin: Double? = null
)