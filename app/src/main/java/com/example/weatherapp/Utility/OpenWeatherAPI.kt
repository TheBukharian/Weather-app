package com.example.weatherapp.Utility

import android.telecom.Call
import com.example.weatherapp.Model.CurrentWeatherResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
const val api_key= "fa867fd33057788a3ff8163c9bcbce3c"

interface OpenWeatherAPI {

    @GET("api.openweathermap.org/data/2.5/weather")
    fun getCurrentWeather(@Query("q") location:String): retrofit2.Call<CurrentWeatherResponse>


}
