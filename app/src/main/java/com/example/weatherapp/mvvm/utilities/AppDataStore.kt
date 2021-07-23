package com.example.weatherapp.mvvm.utilities

import android.content.Context
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.edit
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











    private object PreferenceKeys {
//        val LOCATION_TITLE = stringPreferencesKey(Constants.LOCATION_TITLE)
//        val PREFIX = stringPreferencesKey(Constants.PREFIX)
//        val SETTINGSALLOW = booleanPreferencesKey(Constants.SETTINGS_ALLOW)
//
//        val CAMERA_ID = booleanPreferencesKey(Constants.CAMERA_ID)
        val LANG_ID = stringPreferencesKey(Constants.LANGUAGE)
        val API = stringPreferencesKey(Constants.WEATHER_API)
    }

}