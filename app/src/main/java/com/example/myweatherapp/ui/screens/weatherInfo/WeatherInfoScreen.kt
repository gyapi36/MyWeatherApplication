package com.example.myweatherapp.ui.screens.weatherInfo

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myweatherapp.R
import com.example.myweatherapp.data.MarkerInfo
import com.example.myweatherapp.data.WeatherAPI.WeatherResult
import com.example.myweatherapp.ui.theme.TopBarBackground
import com.example.myweatherapp.ui.theme.WeatherBackground
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import java.util.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherInfoScreen(
    cityName: String,
    context: Context = LocalContext.current,
    weatherInfoViewModel: WeatherInfoViewModel = viewModel(),
    navController: NavController,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(onClick = {
                            navController.navigateUp()
                        }) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = context.getString(R.string.back_arrow_desc),
                                tint = Color.White
                            )
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = context.getString(R.string.live_data_text),
                            color = Color.White,
                            fontFamily = weatherInfoViewModel.myFont,
                            style = TextStyle(fontSize = 24.sp)
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = TopBarBackground
            ),
            actions = {
                IconButton(onClick = {
                    weatherInfoViewModel.getWeather(
                        cityName,
                        context.getString(R.string.metric_param),
                        context.getString(R.string.api_key_param)
                    )
                }) {
                    Icon(
                        Icons.Filled.Refresh,
                        contentDescription = context.getString(R.string.refresh_icon_desc),
                        tint = Color.White
                    )
                }
            })

        when (weatherInfoViewModel.weatherUiState) {
            is WeatherUiState.Init -> {}
            is WeatherUiState.Loading -> CircularProgressIndicator()
            is WeatherUiState.Error -> Text(text = (weatherInfoViewModel.weatherUiState as WeatherUiState.Error).errorMsg)
            is WeatherUiState.Success -> ResultView(
                weatherResult = (weatherInfoViewModel.weatherUiState as WeatherUiState.Success).weatherResult
            )
        }
    }
}

@Composable
fun ResultView(
    weatherResult: WeatherResult,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        MyMapScreen(weatherResult)
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MyMapScreen(
    weatherResult: WeatherResult,
    weatherInfoViewModel: WeatherInfoViewModel = viewModel(),
    context: Context = LocalContext.current,
) {
    val cameraState = rememberCameraPositionState {
        CameraPosition.fromLatLngZoom(
            LatLng(weatherResult.coord?.lat!!, weatherResult.coord.lon!!),
            10f
        )
    }

    weatherInfoViewModel.markerInfo.add(
        MarkerInfo(
            weatherResult.name.toString(),
            weatherResult.coord?.lat!!,
            weatherResult.coord.lon!!,
            weatherResult.main?.temp,
            weatherResult.main?.humidity,
            weatherResult.clouds?.all
        )
    )

    val coroutineScope = rememberCoroutineScope()

    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.NORMAL,
                isTrafficEnabled = false,
                mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                    context, R.raw.mymapconfig
                )
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(0.1f)
                .fillMaxWidth()
                .background(WeatherBackground)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(
                        context.getString(
                            R.string.async_img_param,
                            weatherResult.weather?.get(0)?.icon
                        )
                    )
                    .crossfade(true)
                    .build(),
                contentDescription = context.getString(R.string.image_desc),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
        }

        val random = Random(System.currentTimeMillis())
        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(weatherResult.coord.lat, weatherResult.coord.lon))
            .zoom(10f + random.nextInt(5))
            .tilt(30f + random.nextInt(15))
            .bearing(-45f + random.nextInt(90))
            .build()
        coroutineScope.launch {
            cameraState.animate(
                CameraUpdateFactory.newCameraPosition(cameraPosition), 2000
            )
        }

        Column(
            modifier = Modifier.weight(0.9f)
        ) {
            GoogleMap(
                cameraPositionState = cameraState,
                uiSettings = weatherInfoViewModel.uiSettings,
                properties = mapProperties,
                onMapClick = {
                    val newCameraPosition = CameraPosition.Builder()
                        .target(it)
                        .zoom(10f + random.nextInt(5))
                        .tilt(30f + random.nextInt(15))
                        .bearing(-45f + random.nextInt(90))
                        .build()
                    //cameraState.position = cameraPosition
                    coroutineScope.launch {
                        cameraState.animate(
                            CameraUpdateFactory.newCameraPosition(newCameraPosition), 2000
                        )
                    }
                    weatherInfoViewModel.getWeatherFromLatLon(
                        it.latitude,
                        it.longitude,
                        context.getString(R.string.metric_param),
                        context.getString(R.string.api_key_param)
                    )
                }
            ) {
                for (marker in weatherInfoViewModel.markerInfo) {
                    Marker(
                        state = MarkerState(position = LatLng(marker.lat, marker.lon)),
                        title = marker.name,
                        snippet = " Temperature: ${marker.temp} °C," +
                                " Humidity: ${marker.humidity} %," +
                                " Clouds: ${marker.clouds} %",
                        draggable = false,
                    )
                }
            }
        }
    }
}
