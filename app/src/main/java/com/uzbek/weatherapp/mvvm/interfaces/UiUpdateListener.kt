package com.uzbek.weatherapp.mvvm.interfaces

import com.example.weatherapp.mvvm.data.WeatherData

interface UiUpdateListener {

    fun onUpdateUi(weatherData: WeatherData)
}