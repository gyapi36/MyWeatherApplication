package com.example.myweatherapp.data.database


import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDAO {
    @Query("SELECT * from city_table")
    fun getAllCities(): Flow<List<CityItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(list: CityItem)

    @Update
    suspend fun update(list: CityItem)

    @Delete
    suspend fun delete(list: CityItem)

    @Query("DELETE from city_table")
    suspend fun deleteAllCities()
}