package com.example.weatherapp


import android.content.Intent
import android.content.Intent.*
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.Utility.EXTRA_CITY
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity(), BottomSheetEx.BottomSheetListener{
    override fun onOptionClick(text: String) {}

    val API: String = "263c55c249bef2c72943bbcc77cb742d"
    var CITY:String?= "Tashkent,UZ"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setIcon(R.mipmap.ic_launcher)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setLogo(R.mipmap.ic_launcher)
        supportActionBar?.setDisplayUseLogoEnabled(true)


        val image = findViewById<ImageView>(R.id.WeatherImage)

        address.text=intent.getStringExtra(EXTRA_CITY)
        address.paintFlags=address.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        val animation1 : Animation=AnimationUtils.loadAnimation(this@MainActivity,R.anim.cloud_move)
        val animation2 : Animation=AnimationUtils.loadAnimation(this@MainActivity,R.anim.fast_rotation)
        val animation3 : Animation=AnimationUtils.loadAnimation(this@MainActivity,R.anim.fade_in)
        val animation4 : Animation=AnimationUtils.loadAnimation(this@MainActivity,R.anim.cloud2)


        vintage4.startAnimation(animation4)

        weatherTask().execute()


        if (WeatherImage.tag == 1){
            image.startAnimation(animation1)
            WeatherImage.setOnClickListener {
                image.startAnimation(animation1)
            }

        }else {
            image.startAnimation(animation3)
            WeatherImage.setOnClickListener {
                image.startAnimation(animation3)
                 Log.d("MainActivity","${WeatherImage.tag}")
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
        addressContainer.setOnClickListener {

            val bottomSheet=BottomSheetEx()
            bottomSheet.show(supportFragmentManager,"BottomSheetEx")
        }

        infoWeather.setOnClickListener {
            val intent=Intent(this,WebPage::class.java)
            startActivity(intent)
        }



    }






    inner class weatherTask : AsyncTask<String, Void, String>() {
                override fun onPreExecute() {
                    super.onPreExecute()


                    /* Showing the ProgressBar, Making the main design GONE */
                    findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
                    findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
                    findViewById<ImageButton>(R.id.imageButton).visibility = View.GONE
                    findViewById<ImageView>(R.id.arrow).visibility = View.GONE


                }

                override fun doInBackground(vararg params: String?): String? {
                    var response:String?
                    var checkCity= intent.getStringExtra(EXTRA_CITY)
                    if (checkCity!=null){
                        CITY=intent.getStringExtra(EXTRA_CITY)
                    }
                    else{
                        CITY="Tashkent,UZ"
                    }
                    try{
                        response = URL("https://api.openweathermap.org/data/2.5/weather?q=${CITY}&units=metric&appid=$API").readText(Charsets.UTF_8)
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
                        val temp = main.getString("temp")
                        val tempInt= temp.toDouble()
                        val partTemp=tempInt.roundToInt()
                        val pressure = main.getString("pressure")
                        val humidity = main.getString("humidity")
                        val sunrise:Long = sys.getLong("sunrise")
                        val sunset:Long = sys.getLong("sunset")
                        val windSpeed = wind.getString("speed")
                        val weatherDescription = weather.getString("description")
                        val address = jsonObj.getString("name")+", "+sys.getString("country")






                        findViewById<TextView>(R.id.address).text = address
                        findViewById<TextView>(R.id.updated_at).text =  updatedAtText
                        findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()
                        findViewById<TextView>(R.id.temp).text = partTemp.toString()+"°C"
                        findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000))
                        findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000))
                        findViewById<TextView>(R.id.wind).text = windSpeed+"km/h"
                        findViewById<TextView>(R.id.pressure).text = pressure
                        findViewById<TextView>(R.id.humidity).text = humidity+"%"
                        val animation1 : Animation=AnimationUtils.loadAnimation(this@MainActivity,R.anim.cloud_move)
                        val animation4 : Animation=AnimationUtils.loadAnimation(this@MainActivity,R.anim.cloud2)



                        if(weatherDescription.capitalize()=="Rain"||weatherDescription.capitalize()=="Light rain"||weatherDescription.capitalize()=="Heavy intensity rain"||weatherDescription.capitalize()=="Heavy intensity shower rain"
                            ||weatherDescription.capitalize()=="Shower rain"||weatherDescription.capitalize()=="Light intensity shower rain"||weatherDescription.capitalize()=="Thunderstorm")
                        {
                            mainBack.setBackgroundResource(R.drawable.bg_rain)
                            WeatherImage.setImageResource(R.drawable.rain)
                            WeatherImage.tag = R.drawable.rain
                            vintage.startAnimation(animation1)
                            vintageo.startAnimation(animation4)





                        }else if(weatherDescription.capitalize()=="Snow"||weatherDescription.capitalize()=="Rain and snow"||weatherDescription.capitalize()=="Light shower snow"||weatherDescription.capitalize()=="Shower snow"){
                            mainBack.setBackgroundResource(R.drawable.bg_snow)
                            WeatherImage.setImageResource(R.drawable.snowflake)
                            WeatherImage.tag = R.drawable.snowflake
                            vintage.startAnimation(animation1)
                            vintageo.startAnimation(animation4)


                        }else if(weatherDescription.capitalize()=="Smoke"||weatherDescription.capitalize()=="Broken clouds"||weatherDescription.capitalize()=="Overcast clouds"||weatherDescription.capitalize()=="Scattered clouds"||weatherDescription.capitalize()=="Mist"||weatherDescription.capitalize()=="Few clouds") {
                            mainBack.setBackgroundResource(R.drawable.bg_rain)
                            WeatherImage.setImageResource(R.drawable.clouds)
                            WeatherImage.tag = R.drawable.clouds
                            vintage.startAnimation(animation1)
                            vintageo.startAnimation(animation4)


                        }else if (weatherDescription.capitalize()=="Clear sky"){
                            mainBack.setBackgroundResource(R.drawable.bg_sun)
                            WeatherImage.tag = 1
                            WeatherImage.setImageResource(R.drawable.sunny)
                            findViewById<ImageView>(R.id.vintage).visibility = View.GONE
                            findViewById<ImageView>(R.id.vintage2).visibility = View.GONE
                            findViewById<ImageView>(R.id.vintageo).visibility = View.GONE
                        }

                         findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                        findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE
                        findViewById<TextView>(R.id.Error).visibility = View.GONE
                        findViewById<ImageView>(R.id.arrow).visibility = View.VISIBLE




                    } catch (e: Exception) {
                        findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                        findViewById<ImageButton>(R.id.imageButton).visibility = View.VISIBLE
                        findViewById<TextView>(R.id.Error).visibility = View.VISIBLE

                    }

                }
            }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nav,menu)
        val item = menu!!.findItem(R.id.switcher)
        item.setActionView(R.layout.switch_layout)
        val mySwitch = item.actionView.findViewById<Switch>(R.id.switchForActionBar)
        mySwitch.setOnCheckedChangeListener { p0, isChecked ->
            if (mySwitch.isChecked) {
                Toast.makeText(this@MainActivity, "Dark Mode", Toast.LENGTH_LONG).show()


                supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.colorAccent)))
                box1.setBackgroundColor(Color.parseColor("#3C242424"))
                box2.setBackgroundColor(Color.parseColor("#3C242424"))
                box3.setBackgroundColor(Color.parseColor("#3C242424"))
                box4.setBackgroundColor(Color.parseColor("#3C242424"))
                box5.setBackgroundColor(Color.parseColor("#3C242424"))
                infoWeather.setBackgroundColor(Color.parseColor("#86242424"))
                mainBack.setBackgroundResource(R.drawable.dark_theme)
                if(status.text=="Rain"
                    ||status.text=="Shower rain"||status.text=="Light intensity shower rain")
                {
                    WeatherImage.setImageResource(R.drawable.rain)




                }else if(status.text=="Snow"){
                    WeatherImage.setImageResource(R.drawable.snowflake)


                }else if(status.text=="Smoke"||status.text=="Broken clouds"||status.text=="Scattered clouds"||status.text=="Mist") {
                    WeatherImage.setImageResource(R.drawable.clouds)


                }else if (status.text=="Overcast clouds"||status.text=="Clear sky"){
                    WeatherImage.setImageResource(R.drawable.sunny)
                }

            } else {
                Toast.makeText(this@MainActivity, "Light Mode", Toast.LENGTH_LONG).show()

                supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.colorPrimaryDark)))
                box1.setBackgroundColor(Color.parseColor("#3CF1EBF1"))
                box2.setBackgroundColor(Color.parseColor("#3CF1EBF1"))
                box3.setBackgroundColor(Color.parseColor("#3CF1EBF1"))
                box4.setBackgroundColor(Color.parseColor("#3CF1EBF1"))
                box5.setBackgroundColor(Color.parseColor("#3CF1EBF1"))
                infoWeather.setBackgroundColor(Color.parseColor("#83FDFDFD"))
                mainBack.setBackgroundResource(R.drawable.bg_sun)
                if(status.text=="Rain"
                    ||status.text=="Shower rain"||status.text=="Light intensity shower rain")
                {
                    mainBack.setBackgroundResource(R.drawable.bg_rain)
                    WeatherImage.setImageResource(R.drawable.rain)




                }else if(status.text=="Snow"){
                    mainBack.setBackgroundResource(R.drawable.bg_snow)
                    WeatherImage.setImageResource(R.drawable.snowflake)


                }else if(status.text=="Smoke"||status.text=="Broken clouds"||status.text=="Scattered clouds"||status.text=="Mist") {
                    mainBack.setBackgroundResource(R.drawable.bg_rain)
                    WeatherImage.setImageResource(R.drawable.clouds)


                }else if (status.text=="Overcast clouds"||status.text=="Clear sky"){
                    mainBack.setBackgroundResource(R.drawable.bg_sun)
                    WeatherImage.setImageResource(R.drawable.sunny)
                }
            }
        }


        return super.onCreateOptionsMenu(menu)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId){
            R.id.shareMenu -> {
               try{
                 val shareIntent =Intent(ACTION_SEND)
                   shareIntent.type = "text/plain"
                   shareIntent.putExtra(EXTRA_SUBJECT,"Weather Info")
                   var shareMessage = "\nLet`s try this Weather application:\n\n"
                   shareMessage = shareMessage+ "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID+ "\n\n"
                   shareIntent.putExtra(EXTRA_TEXT,shareMessage)
                   startActivity(createChooser(shareIntent,"Share with "))
               }
               catch (e:Exception){
                   Log.d("MainActivity","Couldn` t load the web site")
               }
            }

            R.id.exitMenu->{
                android.os.Process.killProcess(android.os.Process.myPid())
                exitProcess(1)
            }
            R.id.More->{
                Toast.makeText(this@MainActivity, "Google Play Opened...", Toast.LENGTH_LONG).show()
            }
        }

        return super.onOptionsItemSelected(item)
    }
        }

