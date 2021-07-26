package com.example.weatherapp.mvvm.utilities

import android.content.Context
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class AppDataStore @Inject constructor(context: Context) {

    // build the dataStore and migrate from sharedPreferences

    private val Context.dataStore by preferencesDataStore(
        name = Constants.WEATHER_DATASTORE,
        produceMigrations = { context ->

            listOf(
                SharedPreferencesMigration(context, Constants.LOCALE_SETTING_PREF),
                SharedPreferencesMigration(context, Constants.PREFERENCE_UTIL),
                SharedPreferencesMigration(context, Constants.PREF_UNIQUE_ID)
            )
        })

    private val dataStore = context.dataStore

    //  setter/getter methods

    fun setAPI(hashCode: String) = runBlocking {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.API] = hashCode
        }
    }


    fun getAPI(): String {
        var value = Constants.API
        runBlocking {
            dataStore.data.first {
                value = it[PreferenceKeys.API] ?: ""
                true
            }
        }

        return value
    }

    fun isFirstLaunch(hashCode: Boolean) = runBlocking {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.LAUNCH] = hashCode
        }
    }


    fun isFirsLaunch(): Boolean {
        var value = true
        runBlocking {
            dataStore.data.first {
                value = it[PreferenceKeys.LAUNCH] ?: true
                true
            }
        }

        return value
    }

    fun setSelectedLanguage(lang: String) = runBlocking{
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.LANG_ID] = lang
        }
    }

    fun getSelectedLanguage(): String {
        var value = ""
        runBlocking {
            dataStore.data.first {
                value = it[PreferenceKeys.LANG_ID] ?: ""
                true
            }
        }

        return value
    }

    fun setSelectedCity(city: String) = runBlocking{
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.CITY] = city
        }
    }

    fun getSelectedCity(): String {
        var value = "Tashkent,UZ"
        runBlocking {
            dataStore.data.first {
                value = it[PreferenceKeys.CITY] ?: "Tashkent,UZ"
                true
            }
        }

        return value
    }

    fun setLatestCountry(id: Int) = runBlocking{
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.CITY_ID] = id
        }
    }

    fun getLatestCountry(): Int {
        var value = 0
        runBlocking {
            dataStore.data.first {
                value = it[PreferenceKeys.CITY_ID] ?: 0
                true
            }
        }

        return value
    }












    private object PreferenceKeys {
//        val LOCATION_TITLE = stringPreferencesKey(Constants.LOCATION_TITLE)
        val LAUNCH = booleanPreferencesKey(Constants.LAUNCH)
        val CITY_ID = intPreferencesKey(Constants.CITY_ID)
        val CITY = stringPreferencesKey(Constants.CITY_KEY)
        val LANG_ID = stringPreferencesKey(Constants.LANGUAGE)
        val API = stringPreferencesKey(Constants.WEATHER_API)
    }

}