package com.uzbek.weatherapp.mvvm.base


data class Resource<out T>(val status: com.uzbek.weatherapp.mvvm.base.Resource.Status, val data: T?, val message: String?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T): com.uzbek.weatherapp.mvvm.base.Resource<T> {
            return com.uzbek.weatherapp.mvvm.base.Resource(
                com.uzbek.weatherapp.mvvm.base.Resource.Status.SUCCESS,
                data,
                null
            )
        }

        fun <T> error(message: String, data: T? = null): com.uzbek.weatherapp.mvvm.base.Resource<T> {
            return com.uzbek.weatherapp.mvvm.base.Resource(
                com.uzbek.weatherapp.mvvm.base.Resource.Status.ERROR,
                data,
                message
            )
        }

        fun <T> loading(data: T? = null): com.uzbek.weatherapp.mvvm.base.Resource<T> {
            return com.uzbek.weatherapp.mvvm.base.Resource(
                com.uzbek.weatherapp.mvvm.base.Resource.Status.LOADING,
                data,
                null
            )
        }
    }
}