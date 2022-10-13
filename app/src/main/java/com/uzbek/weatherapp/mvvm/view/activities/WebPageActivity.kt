package com.uzbek.weatherapp.mvvm.view.activities

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.databinding.ActivityWebPageBinding
import java.net.URL

class WebPageActivity : AppCompatActivity() {

    lateinit var mWebView: WebView
    private lateinit var webBinding: ActivityWebPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        webBinding = ActivityWebPageBinding.inflate(layoutInflater)
        setContentView(webBinding.root)
        webtask().execute()

    }

    inner class webtask() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()

            webBinding.apply {
                loaderWeb.visibility=View.VISIBLE
                webWeather.visibility=View.GONE
            }


        }

        @SuppressLint("SetJavaScriptEnabled")
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            webBinding.apply {
                try {

                    mWebView = findViewById(R.id.webWeather)
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
