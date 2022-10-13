package com.uzbek.weatherapp.mvvm.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table", indices = [Index(value = ["country_id","city"], unique = true)])
data class WeatherData(

    @PrimaryKey(autoGenerate = false)
    val country_id: Int,
    val country: String,
    val temp: Double,
    val temp_max: Double,
    val temp_min: Double,
    val humidity: Int,
    val pressure: Int,
    val lat: Double,
    val lon: Double,
    val visibility: Int,
    val sunrise: Int,
    val sunset: Int,
    val type: Int,
    val description: String,
    val wind_speed: Double,
    val wind_deg: Int,
    val date: String,
    val city: String


)