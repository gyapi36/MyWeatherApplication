package com.example.myweatherapp.navigation

sealed class Screen(val route: String) {
    object City : Screen("city")
    object Weather : Screen("weather/{cityName}")
}