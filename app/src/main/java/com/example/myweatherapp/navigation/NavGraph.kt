package com.example.myweatherapp.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myweatherapp.R
import com.example.myweatherapp.ui.screens.citySelect.CitySelectScreen
import com.example.myweatherapp.ui.screens.weatherInfo.WeatherInfoScreen

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    context: Context = LocalContext.current,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.City.route
    ) {
        composable(Screen.City.route) {
            CitySelectScreen(onCitySelected = {cityName ->
                navController.navigate(context.getString(R.string.weather_nav_text, cityName))
            })
        }
        composable(
            Screen.Weather.route,
            arguments = listOf(
                navArgument(context.getString(R.string.cityname_nav_text)){type = NavType.StringType},
            )
        ) {
            val cityName = it.arguments?.getString(context.getString(R.string.cityname_nav_text))

            if (cityName != null) {
                WeatherInfoScreen(cityName = cityName, navController = navController)
            }
        }
    }
}