package com.example.weatherapp.mvvm.view.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.R
import com.example.weatherapp.databinding.MainFragmentBinding
import com.example.weatherapp.mvvm.base.Resource
import com.example.weatherapp.mvvm.data.WeatherData
import com.example.weatherapp.mvvm.extensions.capitalize
import com.example.weatherapp.mvvm.extensions.viewLifecycleLazy
import com.example.weatherapp.mvvm.utilities.App
import com.example.weatherapp.mvvm.view.activities.WebPageActivity
import com.example.weatherapp.mvvm.viewmodel.MainViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import org.joda.time.format.DateTimeFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt
import kotlin.system.exitProcess

class MainFragment : BaseFragment(R.layout.main_fragment) {

    private val binding by viewLifecycleLazy { MainFragmentBinding.bind(requireView()) }

    private val mainViewModel by viewModels<MainViewModel>()

    private lateinit var weatherJob: CompletableJob
    private var requestMode = 2

    lateinit var animation1 : Animation
    lateinit var animation2 : Animation
    lateinit var animation3 : Animation
    lateinit var animation4 : Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!this::weatherJob.isInitialized) {
            weatherJob = Job()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        animation1 = AnimationUtils.loadAnimation(requireActivity(),
            R.anim.cloud_move
        )
        animation2 = AnimationUtils.loadAnimation(requireActivity(),
            R.anim.fast_rotation
        )
        animation3 = AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_in)

        animation4 = AnimationUtils.loadAnimation(requireActivity(), R.anim.cloud2)

        val image = binding.WeatherImage
        binding.address.text = arguments?.getString("EXTRA_CITY")
        binding.address.paintFlags = binding.address.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.vintage4.startAnimation(animation4)


//        setupEverything()

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
            requestWeather(requestMode)
        }
        binding.imageButton.setOnClickListener {
            requestWeather(requestMode)
        }
        binding.addressContainer.setOnClickListener {

            val bottomSheet= BottomSheetExFragment()
            bottomSheet.show(requireActivity().supportFragmentManager,"BottomSheetEx")
        }

        binding.infoWeather.setOnClickListener {
            val intent = Intent(requireContext(), WebPageActivity::class.java)
            startActivity(intent)
        }


    }

    private fun requestWeather(mode: Int) = CoroutineScope(Dispatchers.Main + weatherJob).launch{
        Log.d("ekoko", "before---")

        mainViewModel.requestWeather(mode).collectLatest {
            Log.d("ekoko", "center---")

            if (it != null){

                when(it.status){
                    Resource.Status.SUCCESS -> {
                        if (it.data != null){
                            Log.d("ekoko", "after---")


                            it.data.let { response ->
                                val job = async{

                                    val weather = WeatherData(
                                        country_id = response.sys.id,
                                        country = response.sys.country,
                                        temp = response.main.temp,
                                        temp_max = response.main.temp_max,
                                        temp_min = response.main.temp_min,
                                        humidity = response.main.humidity,
                                        pressure = response.main.pressure,
                                        lat = response.coord.lat,
                                        lon = response.coord.lon,
                                        visibility = response.visibility,
                                        sunrise = response.sys.sunrise,
                                        sunset = response.sys.sunset,
                                        type = response.sys.type,
                                        description = response.weather.last().description,
                                        wind_speed = response.wind.speed,
                                        wind_deg = response.wind.deg,
                                        date = response.dt.toString(),
                                        city = response.name
                                    )

                                    mainViewModel.saveToDatabase(weather)
                                    updateUi(weather)
                                }

                                job.await()
                                if(job.isCompleted){
                                    weatherJob.cancel()
                                }
                            }
                        }
                    }

                    Resource.Status.ERROR -> {
                        Toast.makeText(
                            requireContext(),
                            "Error on request from Api",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                    Resource.Status.LOADING -> {
                        binding.loader.visibility = View.VISIBLE
                        binding.mainContainer.visibility = View.GONE
                        binding.imageButton.visibility = View.GONE
                        binding.arrow.visibility = View.GONE
                    }
                }

            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun updateUi(weather: WeatherData){
        binding.apply {

            val date = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm a")
                .parseDateTime(weather.date)
            val sunriseTime = DateTimeFormat.forPattern("hh:mm a")
                .parseDateTime(weather.sunrise.toString())
            val sunsetTime = DateTimeFormat.forPattern("hh:mm a")
                .parseDateTime(weather.sunset.toString())
            val tempNum = weather.temp.roundToInt()

            address.text = "${weather.city},${weather.country}"
            updatedAt.text = "Updated at: $date"
            status.text = weather.description
            temp.text = "${tempNum}°C"
            sunrise.text = sunriseTime.toString()
            sunset.text = sunsetTime.toString()
            wind.text = "${weather.wind_speed} km/h"
            pressure.text = weather.pressure.toString()
            humidity.text = "${weather.humidity}%"
        }

        val animation1 : Animation=AnimationUtils.loadAnimation(requireActivity(),
            R.anim.cloud_move
        )
        val animation4 : Animation=AnimationUtils.loadAnimation(requireActivity(),
            R.anim.cloud2
        )


        weather.description.let {

            if(it.capitalize()=="Rain"||it.capitalize()=="Light rain"||it.capitalize()=="Heavy intensity rain"||it.capitalize()=="Heavy intensity shower rain"
                ||it.capitalize()=="Shower rain"||it.capitalize()=="Light intensity shower rain"||it.capitalize()=="Thunderstorm")
            {
                binding.mainBack.setBackgroundResource(R.drawable.bg_rain)
                binding.WeatherImage.setImageResource(R.drawable.rain)
                binding.WeatherImage.tag = R.drawable.rain
                binding.vintage.startAnimation(animation1)
                binding.vintageo.startAnimation(animation4)





            }else if(it.capitalize()=="Snow"||it.capitalize()=="Rain and snow"||it.capitalize()=="Light shower snow"||it.capitalize()=="Shower snow"){
                binding.apply {
                    mainBack.setBackgroundResource(R.drawable.bg_snow)
                    WeatherImage.setImageResource(R.drawable.snowflake)
                    WeatherImage.tag = R.drawable.snowflake
                    vintage.startAnimation(animation1)
                    vintageo.startAnimation(animation4)
                }


            }else if(it.capitalize()=="Smoke"||it.capitalize()=="Broken clouds"||it.capitalize()=="Overcast clouds"||it.capitalize()=="Scattered clouds"||it.capitalize()=="Mist"||it.capitalize()=="Few clouds") {
                binding.apply {
                    mainBack.setBackgroundResource(R.drawable.bg_rain)
                    WeatherImage.setImageResource(R.drawable.clouds)
                    WeatherImage.tag = R.drawable.clouds
                    vintage.startAnimation(animation1)
                    vintageo.startAnimation(animation4)
                }


            }else if (it.capitalize()=="Clear sky"){
                binding.apply {
                    mainBack.setBackgroundResource(R.drawable.bg_sun)
                    WeatherImage.tag = 1
                    WeatherImage.setImageResource(R.drawable.sunny)
                    vintage.visibility = View.GONE
                    vintage2.visibility = View.GONE
                    vintageo.visibility = View.GONE
                }
            }else{
                binding.apply {
                    mainBack.setBackgroundResource(R.drawable.bg_sun)
                    WeatherImage.tag = 1
                    WeatherImage.setImageResource(R.drawable.sunny)
                    vintage.visibility = View.GONE
                    vintage2.visibility = View.GONE
                    vintageo.visibility = View.GONE
                }

            }
        }



        binding.apply {
            loader.visibility = View.GONE
            mainContainer.visibility = View.VISIBLE
            Error.visibility = View.GONE
            arrow.visibility = View.VISIBLE
        }
    }

    private fun setupEverything(){

        if (App.isConnectedToInternet(requireContext())){
            requestWeather(requestMode)
        }else{
            mainViewModel.getLastWeather().observe(viewLifecycleOwner){
                it.let {

                if (it != null){
                    updateUi(it)
                }else{
                    Toast.makeText(requireContext(), "No such a weather was found", Toast.LENGTH_SHORT).show()
                }
            }
            }
        }
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_nav,menu)
        val item = menu.findItem(R.id.switcher)
        item.setActionView(R.layout.switch_layout)
        val mySwitch = item.actionView.findViewById<SwitchCompat>(R.id.switchForActionBar)
        binding.apply {

            mySwitch.setOnCheckedChangeListener { p0, isChecked ->

                if (mySwitch.isChecked) {
                    Toast.makeText(requireContext(), "Dark Mode", Toast.LENGTH_LONG).show()

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
                    Toast.makeText(requireContext(), "Light Mode", Toast.LENGTH_LONG).show()

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

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId){
            R.id.shareMenu -> {
                try{
                    val shareIntent =Intent(Intent.ACTION_SEND)
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Weather Info")
                    var shareMessage = "\nLet`s try this Weather application:\n\n"
                    shareMessage = shareMessage+ "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n"
                    shareIntent.putExtra(Intent.EXTRA_TEXT,shareMessage)
                    startActivity(Intent.createChooser(shareIntent, "Share with "))
                }
                catch (e:Exception){
                    Log.d("MainActivity","Couldn` t load the web site")
                }
            }

            R.id.exitMenu ->{
                android.os.Process.killProcess(android.os.Process.myPid())
                exitProcess(1)
            }
            R.id.More ->{
                Toast.makeText(requireContext(), "Google Play Opened...", Toast.LENGTH_LONG).show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!weatherJob.isCancelled){
            weatherJob.cancel()
        }
    }

}