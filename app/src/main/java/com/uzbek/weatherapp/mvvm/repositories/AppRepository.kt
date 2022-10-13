package com.uzbek.weatherapp.mvvm.repositories

import android.util.Log
import androidx.lifecycle.liveData
import com.example.weatherapp.mvvm.view.activities.MainActivity
import com.uzbek.weatherapp.mvvm.base.BaseDataSource
import com.uzbek.weatherapp.mvvm.base.Resource
import com.example.weatherapp.mvvm.data.WeatherData
import com.example.weatherapp.mvvm.utilities.AppDataStore
import com.example.weatherapp.mvvm.db.WeatherDatabase
import com.example.weatherapp.mvvm.net.AppRetrofitService
import com.example.weatherapp.mvvm.utilities.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val appService: AppRetrofitService,
    private val appDatabase: WeatherDatabase,
    private val appDataStore: AppDataStore,
    private val mainActivity: MainActivity
) : com.uzbek.weatherapp.mvvm.base.BaseDataSource() {


    fun isFirstLaunch():Boolean = appDataStore.isFirsLaunch()
    fun isFirstLaunch(v: Boolean) = appDataStore.isFirstLaunch(v)

    fun getApi():String = appDataStore.getAPI()

    fun setApi(api: String) = appDataStore.setAPI(api)

    suspend fun saveWeather(weatherData: WeatherData) {
        appDatabase.weatherDao().insert(weatherData)
    }

    suspend fun updateWeather(weatherData: WeatherData) {
        appDatabase.weatherDao().updateWeather(weatherData)
    }

     fun selectWeatherFromDb(city: String) = flow {
         emit(appDatabase.weatherDao().getWeatherById(city))
     }.flowOn(Dispatchers.IO)

    fun setLatestCountryID(id:Int){
        appDataStore.setLatestCountry(id)
    }

    fun getLatestCountryID(): Int {
        return appDataStore.getLatestCountry()
    }

    fun getSelectedCity() = appDataStore.getSelectedCity()


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
                appDataStore.getSelectedCity().let { city ->
                     params["q"] = city
                }
            }

        }

        appDataStore.getSelectedLanguage().let {
            if (it.isNotEmpty() && Constants.SUPPORTED_LANGUAGES.contains(it)){
            params["lang"] = it
        }
        }

        emit(getResult {
            appService.requestWeatherDataAsync(params)
        })

    }.onStart {
        com.uzbek.weatherapp.mvvm.base.Resource.loading(null)
    }.flowOn(Dispatchers.IO).catch {
        Log.d("ekoko", "message error: $it")
    }

    fun setSelectedCity(string: String){
        appDataStore.setSelectedCity(string)
    }







}