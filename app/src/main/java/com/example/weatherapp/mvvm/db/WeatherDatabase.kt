package com.example.weatherapp.mvvm.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherapp.mvvm.db.dao.WeatherDao

@Database(entities = [WeatherDao::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao
}