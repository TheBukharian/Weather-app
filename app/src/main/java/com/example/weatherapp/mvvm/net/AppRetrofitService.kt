package com.example.weatherapp.mvvm.net

import com.example.weatherapp.mvvm.data.WeatherResult
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.QueryMap

@JvmSuppressWildcards
interface AppRetrofitService {

    @POST("data/2.5/weather")
    suspend fun requestWeatherDataAsync(
        @QueryMap params: Map<String, Any>
    ): Response<WeatherResult>

}