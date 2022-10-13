package com.uzbek.weatherapp.mvvm.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.uzbek.weatherapp.mvvm.base.Resource
import com.example.weatherapp.mvvm.data.WeatherData
import com.example.weatherapp.mvvm.repositories.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.coroutines.coroutineContext


@HiltViewModel
class MainViewModel @Inject constructor(
    private val appRepository: AppRepository
) : ViewModel() {

    @ExperimentalCoroutinesApi
    fun requestWeather(mode: Int) = channelFlow {
        appRepository.requestWeather(mode).collectLatest {
            send(it)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun saveToDatabase(weather: WeatherData){
        appRepository.saveWeather(weather)
    }

    fun getWeatherByCityName(city: String) = liveData(viewModelScope.coroutineContext + Dispatchers.IO)  {
         appRepository.selectWeatherFromDb(city).collect {
             emit(it)
         }
    }

    fun getLastWeather() = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        appRepository.selectWeatherFromDb(appRepository.getSelectedCity()).collect {
            emit(it)
        }
    }

    fun setSelectedCityName(name: String){
        appRepository.setSelectedCity(name)
    }


}