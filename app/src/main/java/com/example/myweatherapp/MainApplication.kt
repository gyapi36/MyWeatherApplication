package com.example.myweatherapp

import android.app.Application
import com.example.myweatherapp.data.CityDatabase

class MainApplication : Application() {
    val database_city: CityDatabase by lazy { CityDatabase.getDatabase(this) }
}