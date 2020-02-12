package com.example.weatherapp

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_web_page.*
import java.net.URL

class WebPage : AppCompatActivity() {

    lateinit var mWebView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_page)

        webtask().execute()

    }

    inner class webtask() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            loaderWeb.visibility=View.VISIBLE
            webWeather.visibility=View.GONE

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {

                mWebView=findViewById(R.id.webWeather)
                mWebView.loadUrl("https://openweathermap.org/city/1512569")
                val webSetting = mWebView.settings
                webSetting.javaScriptEnabled=true
                mWebView.webViewClient= WebViewClient()

                loaderWeb.visibility=View.GONE
                webWeather.visibility=View.VISIBLE
            }
            catch(e:Exception) {

                webWeather.visibility=View.VISIBLE
            }

        }

        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            try{
                response = URL("https://openweathermap.org/city/1512569").readText(
                    Charsets.UTF_8
                )
            }catch (e: Exception){
                response = null
            }
            return response
        }
    }
}
