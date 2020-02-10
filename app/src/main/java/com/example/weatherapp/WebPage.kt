package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient

class WebPage : AppCompatActivity() {

    lateinit var mWebView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_page)
                mWebView=findViewById(R.id.webWeather)
        mWebView.loadUrl("https://openweathermap.org/city/1512569")
        val webSetting = mWebView.settings
        webSetting.javaScriptEnabled=true
        mWebView.webViewClient= WebViewClient()

    }
    override fun onBackPressed(){
        if (mWebView.canGoBack())
        {
            mWebView.goBack()
        }
        else
        {
            super.onBackPressed()
        }
    }
}
