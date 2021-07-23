package com.example.weatherapp.mvvm.net

import com.example.weatherapp.mvvm.utilities.AppDataStore
import com.example.weatherapp.mvvm.utilities.Constants
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class AuthInterceptor
@Inject constructor(
    private val appDataStore: AppDataStore

    ): Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newUrl = request.url().newBuilder()
            .scheme("https")
            .host(Constants.BASE_URL)
            .build()

        val newRequest = request.newBuilder()


        return chain.proceed(
            newRequest
                .url(newUrl)
                .build()
        )

    }
}