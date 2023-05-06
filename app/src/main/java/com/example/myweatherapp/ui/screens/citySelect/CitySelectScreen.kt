package com.example.myweatherapp.ui.screens.citySelect

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myweatherapp.R
import com.example.myweatherapp.data.database.CityItem
import com.example.myweatherapp.ui.theme.TopBarBackground
import com.example.myweatherapp.ui.theme.WhiteBack

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitySelectScreen(
    context: Context = LocalContext.current,
    citySelectViewModel: CitySelectViewModel = viewModel(factory = CitySelectViewModel.factory),
    onCitySelected: (String) -> Unit
) {

    val cityList by citySelectViewModel.getAllCity().collectAsState(initial = emptyList())

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.globe),
                                contentDescription = context.getString(R.string.globe_icon_desc),
                                modifier = Modifier
                                    .size(50.dp)
                                    .padding(end = 10.dp)
                                    .background(TopBarBackground)
                            )
                            Text(
                                text = context.getString(R.string.app_name_text),
                                color = Color.White,
                                fontFamily = citySelectViewModel.myFont,
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
                        citySelectViewModel.clearAllCities()
                    }) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = context.getString(R.string.delete_all_button_desc),
                            tint = Color.White
                        )
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .padding(top = 70.dp)
            ) {
                if (citySelectViewModel.showDialog) {
                    AddNewCity(
                        onDialogClose = {
                            citySelectViewModel.showDialog = false
                        }
                    )
                }
                LazyColumn {
                    items(cityList) { item ->
                        CityCard(cityItem = item,
                            onRemoveItem = {
                                citySelectViewModel.removeShopList(item)
                            },
                            onOpenItem = {
                                onCitySelected(item.cityName)
                            }
                        )
                    }
                }

            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    citySelectViewModel.showDialog = true
                },
                containerColor = TopBarBackground,
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .size(100.dp)
                    .padding(5.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = context.getString(R.string.fab_desc),
                    tint = Color.White,
                )
            }
        }
    )
}

@Composable
fun AddNewCity(
    onDialogClose: () -> Unit = {},
    citySelectViewModel: CitySelectViewModel = viewModel(),
    context: Context = LocalContext.current,
) {
    fun validate() {
        citySelectViewModel.cityNameErrorState = TextUtils.isEmpty(citySelectViewModel.cityName)

        if (citySelectViewModel.cityNameErrorState) {
            citySelectViewModel.errorText = context.getString(R.string.card_description_text)
        }
    }

    Dialog(
        onDismissRequest = onDialogClose
    ) {
        Surface(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .height(150.dp),
            shape = RoundedCornerShape(size = 5.dp)
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = citySelectViewModel.cityName,
                    onValueChange = {
                        citySelectViewModel.cityName = it
                        validate()
                    },
                    isError = citySelectViewModel.cityNameErrorState,
                    label = {
                        Text(text = context.getString(R.string.city_name_label))
                    },
                    trailingIcon = {
                        if (citySelectViewModel.cityNameErrorState) {
                            Icon(
                                Icons.Filled.Warning,
                                contentDescription = context.getString(R.string.error_desc),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    modifier = Modifier
                        .padding(5.dp)
                )

                if (
                    citySelectViewModel.cityNameErrorState
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = citySelectViewModel.errorText,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    Button(
                        onClick = {
                            onDialogClose()
                        },
                        colors = ButtonDefaults.buttonColors(TopBarBackground),
                        modifier = Modifier
                            .padding(5.dp)
                            .background(TopBarBackground)
                    ) {
                        Text(
                            text = context.getString(R.string.cancel_text),
                            fontFamily = citySelectViewModel.myFont
                        )
                    }
                    Button(
                        onClick = {
                            validate()
                            if (!citySelectViewModel.cityNameErrorState) {
                                citySelectViewModel.addCity(
                                    CityItem(
                                        cityName = citySelectViewModel.cityName
                                    )
                                )
                                onDialogClose()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(TopBarBackground),
                        modifier = Modifier
                            .padding(5.dp)
                            .background(TopBarBackground)

                    ) {
                        Text(
                            text = context.getString(R.string.add_city_text),
                            fontFamily = citySelectViewModel.myFont
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CityCard(
    cityItem: CityItem,
    onRemoveItem: () -> Unit = {},
    onOpenItem: (String) -> Unit = {},
    citySelectViewModel: CitySelectViewModel = viewModel(factory = CitySelectViewModel.factory),
    context: Context = LocalContext.current
) {
    OutlinedCard(
        colors =
        CardDefaults.cardColors(
            containerColor = WhiteBack
        ),
        shape = RoundedCornerShape(20.dp),
        elevation =
        CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier
            .padding(2.dp)
            .clickable {
                onOpenItem(cityItem.cityName)
            }

    ) {
        Column(
            modifier = Modifier
                .padding(5.dp)
                .animateContentSize()
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = cityItem.cityName,
                        fontSize = 20.sp,
                        fontFamily = citySelectViewModel.myFont,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = context.getString(R.string.click_here_to_watch_report_text),
                        fontSize = 16.sp,
                        fontFamily = citySelectViewModel.myFont,
                        fontWeight = FontWeight.Light,
                        color = Color.Gray
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.x),
                        contentDescription = context.getString(R.string.x_icon_desc),
                        modifier = Modifier
                            .size(50.dp)
                            .padding(end = 10.dp)
                            .background(WhiteBack)
                            .clickable {
                                onRemoveItem()
                            }
                    )
                }
            }
        }
    }
}