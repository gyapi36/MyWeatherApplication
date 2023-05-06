package com.example.myweatherapp.ui.screens.citySelect

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myweatherapp.MainApplication
import com.example.myweatherapp.R
import com.example.myweatherapp.data.database.CityDAO
import com.example.myweatherapp.data.database.CityItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CitySelectViewModel(
    private val cityDAO: CityDAO
) : ViewModel() {

    var showDialog by mutableStateOf(false)
    var cityName by mutableStateOf("")

    var cityNameErrorState by mutableStateOf(false)
    var errorText by mutableStateOf("")

    val myFont = FontFamily(Font(R.font.king_font))

    fun getAllCity(): Flow<List<CityItem>> {
        return cityDAO.getAllCities()
    }

    fun addCity(cityItem: CityItem) {
        viewModelScope.launch(Dispatchers.IO) {
            cityDAO.insert(cityItem)
        }
    }

    fun removeShopList(cityItem: CityItem) {
        viewModelScope.launch(Dispatchers.IO) {
            cityDAO.delete(cityItem)
        }
    }

    fun clearAllCities() {
        viewModelScope.launch(Dispatchers.IO) {
            cityDAO.deleteAllCities()
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MainApplication)
                CitySelectViewModel(cityDAO = application.database_city.cityDao())
            }
        }
    }
}