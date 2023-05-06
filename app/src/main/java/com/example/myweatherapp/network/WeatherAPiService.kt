package com.example.myweatherapp.network

import com.example.myweatherapp.data.WeatherAPI.WeatherResult
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

// https://api.openweathermap.org/data/2.5/weather?q=Budapest,hu&units=metric&appid=0c4fdc7ce412fc6190d2983369144d6c

private const val BASE_URL =
    "http://api.openweathermap.org/data/2.5/"

@OptIn(ExperimentalSerializationApi::class)
private val retrofit = Retrofit.Builder()
    .addConverterFactory(
        Json.asConverterFactory(
            "application/json".toMediaType()
        )
    )
    .baseUrl(BASE_URL)
    .build()

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object WeatherApi {
    val retrofitService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}

interface WeatherApiService {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("units") unit: String,
        @Query("appid") appid: String
    ): WeatherResult

    @GET("weather")
    suspend fun getWeatherFomLatLon(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") unit: String,
        @Query("appid") appid: String
    ): WeatherResult
}