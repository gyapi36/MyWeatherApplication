package com.example.myweatherapp.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city_table")
data class CityItem(
    @PrimaryKey(autoGenerate = true) val listId: Int = 0,
    @ColumnInfo(name = "city_name") val cityName: String,
)