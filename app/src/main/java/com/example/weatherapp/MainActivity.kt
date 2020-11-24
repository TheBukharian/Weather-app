package com.example.weatherapp


import android.content.Intent
import android.content.Intent.*
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.Utility.EXTRA_CITY
import com.example.weatherapp.net.RequestAPI
import com.flaviofaria.kenburnsview.BuildConfig
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.math.roundToInt
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity(), BottomSheetEx.BottomSheetListener{
    override fun onOptionClick(text: String) {}

    val API: String = "263c55c249bef2c72943bbcc77cb742d"
    var CITY:String = "Tashkent"
    val TAG = "Belle DELPHINE"


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
        getCurrentWeather()

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
            getCurrentWeather()


        }
        imageButton.setOnClickListener {
            getCurrentWeather()
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
   private fun getCurrentWeather(){
       var checkCity= intent.getStringExtra(EXTRA_CITY)
       if (checkCity!=null){
           CITY=intent.getStringExtra(EXTRA_CITY)
       }
       else{
           CITY="Tashkent,UZ"
       }


       val client=OkHttpClient.Builder().apply {
           addInterceptor(Interceptor { chain ->
               val builder=chain.request().newBuilder()
               builder.header("appid","fa867fd33057788a3ff8163c9bcbce3c")
               builder.header("units","metric")
               return@Interceptor chain.proceed(builder.build())
           })
       }
       val api = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org")
            .addConverterFactory(GsonConverterFactory.create()).client(client.build())
            .build()
            .create(RequestAPI::class.java)

       GlobalScope.launch(Dispatchers.IO){
           val response = api.getCurrentWeather("/data/2.5/weather",CITY).awaitResponse()

                if (response.isSuccessful){
                    val data = response.body()!!

                    withContext(Dispatchers.Main){
                        address.text = data.name
                        val tempInt= data.main.temp.toDouble()
                        val tempR=tempInt.roundToInt()
                        temp.text = tempR.toString()

                        Log.d(TAG,data.sys.country)
                        Log.d(TAG,tempR.toString())

                    }
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

