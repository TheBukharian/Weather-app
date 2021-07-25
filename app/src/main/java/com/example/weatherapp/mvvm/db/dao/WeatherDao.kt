package com.example.weatherapp.mvvm.db.dao

import androidx.room.*
import com.example.weatherapp.mvvm.data.WeatherData

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weather: WeatherData)

    @Query("SELECT * FROM weather_table WHERE country_id = :countryId")
    suspend fun getWeatherById(countryId: Int): WeatherData?

    @Update
    suspend fun updateWeather(countryData: WeatherData)

}