package com.uzbek.weatherapp.mvvm.base

import android.util.Log
import retrofit2.Response


abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): com.uzbek.weatherapp.mvvm.base.Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return com.uzbek.weatherapp.mvvm.base.Resource.Companion.success(
                    body
                )
            }
            return error(" ${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): com.uzbek.weatherapp.mvvm.base.Resource<T> {
        Log.e("remoteDataSource", message)
        return com.uzbek.weatherapp.mvvm.base.Resource.Companion.error(message)
    }

}