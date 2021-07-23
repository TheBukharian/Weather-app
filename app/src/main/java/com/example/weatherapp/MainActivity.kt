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
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.mvvm.utilities.AppDataStore
import com.example.weatherapp.mvvm.utilities.Constants
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.system.exitProcess


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BottomSheetEx.BottomSheetListener{

    @Inject
    lateinit var appDataStore: AppDataStore


    private lateinit var binding: ActivityMainBinding
    lateinit var City: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setIcon(R.mipmap.ic_launcher)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setLogo(R.mipmap.ic_launcher)
        supportActionBar?.setDisplayUseLogoEnabled(true)


        val image = findViewById<ImageView>(R.id.WeatherImage)

        binding.address.text = intent.getStringExtra("EXTRA_CITY")
        binding.address.paintFlags = binding.address.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        val animation1 : Animation=AnimationUtils.loadAnimation(this@MainActivity,R.anim.cloud_move)
        val animation2 : Animation=AnimationUtils.loadAnimation(this@MainActivity,R.anim.fast_rotation)
        val animation3 : Animation=AnimationUtils.loadAnimation(this@MainActivity,R.anim.fade_in)
        val animation4 : Animation=AnimationUtils.loadAnimation(this@MainActivity,R.anim.cloud2)


        binding.vintage4.startAnimation(animation4)

        weatherTask().execute()


        if (binding.WeatherImage.tag == 1){
            image.startAnimation(animation1)
            binding.WeatherImage.setOnClickListener {
                image.startAnimation(animation1)
            }

        }else {
            image.startAnimation(animation3)
            binding.WeatherImage.setOnClickListener {
                image.startAnimation(animation3)
                 Log.d("MainActivity","${binding.WeatherImage.tag}")
            }
        }


        binding.box1.setOnClickListener {
            binding.box1.startAnimation(animation2)
        }
        binding.box2.setOnClickListener {
            binding.box2.startAnimation(animation2)
        }
        binding.box3.setOnClickListener {
            binding.box3.startAnimation(animation2)
        }
        binding.box4.setOnClickListener {
            binding.box4.startAnimation(animation2)
        }
        binding.box5.setOnClickListener {
            binding.box5.startAnimation(animation2)
        }
        binding.infoWeather.setOnClickListener {
            binding.infoWeather.startAnimation(animation2)
        }

        binding.weatherUpdateBtn.setOnClickListener {
            weatherTask().execute()


        }
        binding.imageButton.setOnClickListener {
            weatherTask().execute()

        }
        binding.addressContainer.setOnClickListener {

            val bottomSheet=BottomSheetEx()
            bottomSheet.show(supportFragmentManager,"BottomSheetEx")
        }

        binding.infoWeather.setOnClickListener {
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
                    val checkCity= intent.getStringExtra("EXTRA_CITY")
                    if (checkCity != null){
                        City = checkCity
                    }
                    else{
                        City = Constants.CITY
                    }
                    try{
                        response = URL("https://api.openweathermap.org/data/2.5/weather?q=${City}&units=metric&appid=${appDataStore.getAPI()}").readText(Charsets.UTF_8)
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
                        findViewById<TextView>(R.id.temp).text = "$partTemp°C"
                        findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000))
                        findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000))
                        findViewById<TextView>(R.id.wind).text = windSpeed+"km/h"
                        findViewById<TextView>(R.id.pressure).text = pressure
                        findViewById<TextView>(R.id.humidity).text = "$humidity%"
                        val animation1 : Animation=AnimationUtils.loadAnimation(this@MainActivity,R.anim.cloud_move)
                        val animation4 : Animation=AnimationUtils.loadAnimation(this@MainActivity,R.anim.cloud2)



                        if(weatherDescription.capitalize()=="Rain"||weatherDescription.capitalize()=="Light rain"||weatherDescription.capitalize()=="Heavy intensity rain"||weatherDescription.capitalize()=="Heavy intensity shower rain"
                            ||weatherDescription.capitalize()=="Shower rain"||weatherDescription.capitalize()=="Light intensity shower rain"||weatherDescription.capitalize()=="Thunderstorm")
                        {
                            binding.mainBack.setBackgroundResource(R.drawable.bg_rain)
                            binding.WeatherImage.setImageResource(R.drawable.rain)
                            binding.WeatherImage.tag = R.drawable.rain
                            binding.vintage.startAnimation(animation1)
                            binding.vintageo.startAnimation(animation4)





                        }else if(weatherDescription.capitalize()=="Snow"||weatherDescription.capitalize()=="Rain and snow"||weatherDescription.capitalize()=="Light shower snow"||weatherDescription.capitalize()=="Shower snow"){
                           binding.apply {
                               mainBack.setBackgroundResource(R.drawable.bg_snow)
                               WeatherImage.setImageResource(R.drawable.snowflake)
                               WeatherImage.tag = R.drawable.snowflake
                               vintage.startAnimation(animation1)
                               vintageo.startAnimation(animation4)
                           }


                        }else if(weatherDescription.capitalize()=="Smoke"||weatherDescription.capitalize()=="Broken clouds"||weatherDescription.capitalize()=="Overcast clouds"||weatherDescription.capitalize()=="Scattered clouds"||weatherDescription.capitalize()=="Mist"||weatherDescription.capitalize()=="Few clouds") {
                            binding.apply {
                                mainBack.setBackgroundResource(R.drawable.bg_rain)
                                WeatherImage.setImageResource(R.drawable.clouds)
                                WeatherImage.tag = R.drawable.clouds
                                vintage.startAnimation(animation1)
                                vintageo.startAnimation(animation4)
                            }


                        }else if (weatherDescription.capitalize()=="Clear sky"){
                            binding.apply {
                                mainBack.setBackgroundResource(R.drawable.bg_sun)
                                WeatherImage.tag = 1
                                WeatherImage.setImageResource(R.drawable.sunny)
                                vintage.visibility = View.GONE
                                vintage2.visibility = View.GONE
                                vintageo.visibility = View.GONE
                            }
                        }

                        binding.apply {
                            loader.visibility = View.GONE
                            mainContainer.visibility = View.VISIBLE
                            Error.visibility = View.GONE
                            arrow.visibility = View.VISIBLE
                        }



                    } catch (e: Exception) {
                        binding.apply {
                            loader.visibility = View.GONE
                            imageButton.visibility = View.VISIBLE
                            Error.visibility = View.VISIBLE
                        }
                    }
                }
            }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nav,menu)
        val item = menu!!.findItem(R.id.switcher)
        item.setActionView(R.layout.switch_layout)
        val mySwitch = item.actionView.findViewById<Switch>(R.id.switchForActionBar)
        binding.apply {
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

    override fun onOptionClick(text: String) {}



}

