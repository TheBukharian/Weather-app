package com.example.weatherapp.net

import com.example.weatherapp.Model.CurrentWeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url

interface RequestAPI {



    @GET
    fun getCurrentWeather(@Url url:String,@Query("q") city:String):Call<CurrentWeatherResponse>

}