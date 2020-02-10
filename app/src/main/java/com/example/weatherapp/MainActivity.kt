package com.example.weatherapp

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    var CITY: String = "tashkent,uz"
    val API: String = "263c55c249bef2c72943bbcc77cb742d"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setIcon(R.mipmap.ic_launcher)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setLogo(R.mipmap.ic_launcher)
        supportActionBar?.setDisplayUseLogoEnabled(true)
        weatherTask().execute()

        val image = findViewById<ImageView>(R.id.WeatherImage)as ImageView
        val animation1 : Animation=AnimationUtils.loadAnimation(this@MainActivity,R.anim.rotation)
        val animation2 : Animation=AnimationUtils.loadAnimation(this@MainActivity,R.anim.fast_rotation)
        val animation3 : Animation=AnimationUtils.loadAnimation(this@MainActivity,R.anim.fade_in)





        image.startAnimation(animation1)

        if (WeatherImage.getTag()==R.drawable.sunny){
            image.startAnimation(animation1)
            WeatherImage.setOnClickListener {
                image.startAnimation(animation1)
            }
        }else {
            image.startAnimation(animation3)


            WeatherImage.setOnClickListener {
                image.startAnimation(animation3)


            }
        }


        box1.setOnClickListener {
            box1.startAnimation(animation2)
        }
        box2.setOnClickListener {
            box2.startAnimation(animation2)
        }
        box3.setOnClickListener {
            box3.startAnimation(animation2)
        }
        box4.setOnClickListener {
            box4.startAnimation(animation2)
        }
        box5.setOnClickListener {
            box5.startAnimation(animation2)
        }
        infoWeather.setOnClickListener {
            infoWeather.startAnimation(animation2)
        }

        weatherUpdateBtn.setOnClickListener {
            weatherTask().execute()

        }
        imageButton.setOnClickListener {
            weatherTask().execute()

        }

        infoWeather.setOnClickListener {
            val intent=Intent(this,WebPage::class.java)
            startActivity(intent)

//            showAlert()
        }
    }

    fun showAlert(){
        val inflater=layoutInflater

        val inflate_view=inflater.inflate(R.layout.activity_web_page,null)
        val alertDialog=AlertDialog.Builder(this)
        alertDialog.setIcon(R.mipmap.ic_launcher).setView(inflate_view)

        val alert = alertDialog.create()
        alert.show()

    }




    inner class weatherTask() : AsyncTask<String, Void, String>() {
                override fun onPreExecute() {
                    super.onPreExecute()
                    /* Showing the ProgressBar, Making the main design GONE */
                    findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
                    findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
                    findViewById<ImageButton>(R.id.imageButton).visibility = View.GONE
                }

                override fun doInBackground(vararg params: String?): String? {
                    var response:String?
                    try{
                        response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API").readText(
                            Charsets.UTF_8
                        )
                    }catch (e: Exception){
                        response = null
                    }
                    return response
                }

                override fun onPostExecute(result: String?) {
                    super.onPostExecute(result)
                    try {
                        /* Extracting JSON returns from the API */
                        val jsonObj = JSONObject(result)
                        val main = jsonObj.getJSONObject("main")
                        val sys = jsonObj.getJSONObject("sys")
                        val wind = jsonObj.getJSONObject("wind")
                        val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                        val updatedAt:Long = jsonObj.getLong("dt")
                        val updatedAtText = "Updated at: "+ SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updatedAt*1000))
                        val temp = main.getString("temp")+"°C"
                        val pressure = main.getString("pressure")
                        val humidity = main.getString("humidity")
                        val sunrise:Long = sys.getLong("sunrise")
                        val sunset:Long = sys.getLong("sunset")
                        val windSpeed = wind.getString("speed")
                        val weatherDescription = weather.getString("description")
                        val address = jsonObj.getString("name")+", "+sys.getString("country")






                        /* Populating extracted data into our views */
                        findViewById<TextView>(R.id.address).text = address
                        findViewById<TextView>(R.id.updated_at).text =  updatedAtText
                        findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()
                        findViewById<TextView>(R.id.temp).text = temp
                        findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000))
                        findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000))
                        findViewById<TextView>(R.id.wind).text = windSpeed+"km/h"
                        findViewById<TextView>(R.id.pressure).text = pressure
                        findViewById<TextView>(R.id.humidity).text = humidity+"%"


                        if(weatherDescription.capitalize()=="Rain"
                            ||weatherDescription.capitalize()=="Shower rain"||weatherDescription.capitalize()=="Light intensity shower rain")
                        {
                            mainBack.setBackgroundResource(R.drawable.bg_rain)
                            WeatherImage.setImageResource(R.drawable.rain)
                            WeatherImage.setTag(R.drawable.rain)


                        }else if(weatherDescription.capitalize()=="Snow"){
                            mainBack.setBackgroundResource(R.drawable.bg_snow)
                            WeatherImage.setImageResource(R.drawable.snowflake)
                            WeatherImage.setTag(R.drawable.snowflake)


                        }else if(weatherDescription.capitalize()=="Smoke"||weatherDescription.capitalize()=="Broken clouds"||weatherDescription.capitalize()=="Scattered clouds"||weatherDescription.capitalize()=="Mist") {
                            mainBack.setBackgroundResource(R.drawable.bg_rain)
                            WeatherImage.setImageResource(R.drawable.clouds)
                            WeatherImage.setTag(R.drawable.clouds)


                        }else if (weatherDescription.capitalize()=="Overcast clouds"||weatherDescription.capitalize()=="Clear sky"){
                            mainBack.setBackgroundResource(R.drawable.bg_sun)
                            WeatherImage.setTag(R.drawable.sunny)
                            WeatherImage.setImageResource(R.drawable.sunny)
                        }

                        /* Views populated, Hiding the loader, Showing the main design */
                        findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                        findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE

                    } catch (e: Exception) {
                        findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                        findViewById<ImageButton>(R.id.imageButton).visibility = View.VISIBLE
                    }

                }
            }

        }

