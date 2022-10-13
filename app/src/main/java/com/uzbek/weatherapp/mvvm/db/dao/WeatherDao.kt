package com.uzbek.weatherapp.mvvm.db.dao

import androidx.room.*
import com.example.weatherapp.mvvm.data.WeatherData

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weather: WeatherData)

    @Query("SELECT * FROM weather_table WHERE city = :city LIMIT 1")
    suspend fun getWeatherById(city: String): WeatherData?

    @Update
    suspend fun updateWeather(countryData: WeatherData)

}