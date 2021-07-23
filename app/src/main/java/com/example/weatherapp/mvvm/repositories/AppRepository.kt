package com.example.weatherapp.mvvm.repositories

import com.example.weatherapp.MainActivity
import com.example.weatherapp.mvvm.base.BaseDataSource
import com.example.weatherapp.mvvm.base.Resource
import com.example.weatherapp.mvvm.data.WeatherData
import com.example.weatherapp.mvvm.utilities.AppDataStore
import com.example.weatherapp.mvvm.db.WeatherDatabase
import com.example.weatherapp.mvvm.net.AppRetrofitService
import com.example.weatherapp.mvvm.utilities.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val appService: AppRetrofitService,
    private val appDatabase: WeatherDatabase,
    private val appDataStore: AppDataStore,
    private val mainActivity: MainActivity
) : BaseDataSource() {


    fun getApi():String = appDataStore.getAPI()

    fun setApi(api: String) = appDataStore.setAPI(api)

    suspend fun saveWeather(weatherData: WeatherData) {
        appDatabase.weatherDao().insert(weatherData)
    }

    suspend fun updateWeather(weatherData: WeatherData) {
        appDatabase.weatherDao().updateWeather(weatherData)
    }

    suspend fun selectWeatherFromDb(countryId: Int): WeatherData =
        appDatabase.weatherDao().getWeatherById(countryId)

    suspend fun requestWeather(mode: Int) = flow {

        val params: HashMap<String,Any> = HashMap()
        params["appid"] = Constants.API



        when(mode){
            1 -> {
                // todo by user Coordinated
                params["lat"] = "// TODO: 23.07.2021 getLatitude "
                params["lon"] = "// TODO: 23.07.2021 getLongitude "
            }

            2-> {
                //todo by country Name
                params["q"] = "Tashkent,UZ"
            }

        }

        appDataStore.getSelectedLanguage().let {
            if (it.isNotEmpty() && Constants.SUPPORTED_LANGUAGES.contains(it)){
            params["lang"] = appDataStore.getSelectedLanguage()
        }
        }

        emit(getResult {
            appService.requestWeatherData(params)
        })

    }.onStart {
        Resource.loading(null)
    }.flowOn(Dispatchers.IO)






}