package com.example.weatherapp.mvvm.utilities

import android.content.Context
import android.net.ConnectivityManager
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: MultiDexApplication(){

    override fun onCreate() {
        super.onCreate()
        instance = this
        MultiDex.install(this)
        Stetho.initializeWithDefaults(this)

    }


    companion object{

        lateinit var instance: App
            private set

        fun isConnectedToInternet(context: Context): Boolean {
            val cm = (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
            return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
        }
    }
}