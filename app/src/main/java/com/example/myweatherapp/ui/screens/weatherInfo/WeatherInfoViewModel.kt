package com.example.myweatherapp.ui.screens.weatherInfo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.R
import com.example.myweatherapp.data.MarkerInfo
import com.example.myweatherapp.data.WeatherAPI.WeatherResult
import com.example.myweatherapp.network.WeatherApi
import com.google.maps.android.compose.MapUiSettings
import kotlinx.coroutines.launch

sealed interface WeatherUiState {
    object Init : WeatherUiState
    object Loading : WeatherUiState
    data class Success(val weatherResult: WeatherResult) : WeatherUiState
    data class Error(val errorMsg: String) : WeatherUiState
}

class WeatherInfoViewModel : ViewModel() {
    var weatherUiState: WeatherUiState by mutableStateOf(WeatherUiState.Init)

    val uiSettings by mutableStateOf(
        MapUiSettings(
            zoomControlsEnabled = true,
            zoomGesturesEnabled = true
        )
    )

    val markerInfo = mutableListOf<MarkerInfo>()

    val myFont = FontFamily(Font(R.font.king_font))

    fun getWeather(city: String, unit: String, appId: String) {
        weatherUiState = WeatherUiState.Loading
        viewModelScope.launch {
            weatherUiState = try {
                val result = WeatherApi.retrofitService.getWeather(city, unit, appId)
                WeatherUiState.Success(result)
            } catch (e: Exception) {
                e.printStackTrace()
                WeatherUiState.Error(e.message.toString())
            }
        }
    }

    fun getWeatherFromLatLon(lat: Double, lon: Double, unit: String, appId: String) {
        weatherUiState = WeatherUiState.Loading
        viewModelScope.launch {
            weatherUiState = try {
                val result = WeatherApi.retrofitService.getWeatherFomLatLon(lat, lon, unit, appId)
                WeatherUiState.Success(result)
            } catch (e: Exception) {
                e.printStackTrace()
                WeatherUiState.Error(e.message.toString())
            }
        }
    }
}