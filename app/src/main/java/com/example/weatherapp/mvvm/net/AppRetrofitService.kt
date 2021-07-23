package com.example.weatherapp.mvvm.net

import com.example.weatherapp.mvvm.data.WeatherResult
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AppRetrofitService {

    @FormUrlEncoded
    @POST("data/2.5/weather")
    suspend fun requestWeatherData(
        @FieldMap params: Map<String, Any>
    ): Response<WeatherResult>

}