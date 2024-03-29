package com.uzbek.weatherapp.mvvm.view.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.uzbek.weatherapp.BuildConfig
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentThirdBinding
import com.uzbek.weatherapp.mvvm.base.Resource
import com.example.weatherapp.mvvm.data.WeatherData
import com.example.weatherapp.mvvm.extensions.capitalize
import com.example.weatherapp.mvvm.extensions.navigateSafe
import com.example.weatherapp.mvvm.interfaces.UiUpdateListener
import com.example.weatherapp.mvvm.utilities.App
import com.example.weatherapp.mvvm.utilities.Constants
import com.example.weatherapp.mvvm.utilities.ProgressButton
import com.uzbek.weatherapp.mvvm.utilities.pulltorefresh.PullToRefreshView
import com.example.weatherapp.mvvm.view.activities.MainActivity
import com.example.weatherapp.mvvm.view.activities.WebPageActivity
import com.example.weatherapp.mvvm.viewmodel.MainViewModel
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt
import kotlin.system.exitProcess


class ThirdFragment : BaseFragment(R.layout.fragment_third) {

    private var _binding: FragmentThirdBinding? = null
    private val binding get() = _binding!!

    private var pullRefreshView: com.uzbek.weatherapp.mvvm.utilities.pulltorefresh.PullToRefreshView? = null
    private val mainViewModel by viewModels<MainViewModel>()

    private var mInterstitialAd: InterstitialAd? = null


    private lateinit var weatherJob: CompletableJob
    private lateinit var uiListener: UiUpdateListener
    private var requestMode = 2
    var isDestroyed = false
    var fromDialog = false


    private lateinit var animation1: Animation
    private lateinit var animation3: Animation
    private lateinit var animation4: Animation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!this::weatherJob.isInitialized) {
            weatherJob = Job()
        }

        MobileAds.initialize(requireContext()) {}
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThirdBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (appRepository.isFirstLaunch()) {
            appRepository.isFirstLaunch(false)
        }

        var adRequest = AdRequest.Builder().build()
        InterstitialAd.load(requireContext(),"ca-app-pub-2343736594040989/1546269088", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })


        setFragmentResultListener("adBanner") { key, bundle ->

            if (mInterstitialAd != null) {
                mInterstitialAd?.show(requireActivity())
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet.")
            }
        }




        binding.adView.adListener = object: AdListener() {
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        }

        pullRefreshView = binding.pullToRefresh

        uiListener = object : UiUpdateListener{
            override fun onUpdateUi(weatherData: WeatherData) {
                updateUi(weatherData)

            }
        }



        pullRefreshView?.setOnRefreshListener {
            pullRefreshView!!.postDelayed({
                requestWeather(requestMode)

            },1500)
        }



        animation1 = AnimationUtils.loadAnimation(
            requireActivity(),
            R.anim.cloud_move
        )


        animation3 = AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_in)

        animation4 = AnimationUtils.loadAnimation(requireActivity(), R.anim.cloud2)

        binding.address.text = arguments?.getString("EXTRA_CITY")
        binding.address.paintFlags = binding.address.paintFlags or Paint.UNDERLINE_TEXT_FLAG






        binding.addressContainer.setOnClickListener {
            findNavController().navigateSafe(R.id.action_thirdFragment_to_bottomSheetExFragment)
        }

        binding.infoWeather.setOnClickListener {
            val intent = Intent(requireContext(), WebPageActivity::class.java)
            startActivity(intent)
        }


    }


    override fun onResume() {
        super.onResume()

        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        setupEverything()

        hideLoading()
    }






    @ExperimentalCoroutinesApi
    private fun requestWeather(mode: Int)  {
        val progressBtn = ProgressButton(requireContext(), binding.progressL)

        weatherJob.cancel()

        weatherJob = Job()
        CoroutineScope(Dispatchers.Main + weatherJob).launch{
            mainViewModel.requestWeather(mode).collectLatest {

                when (it.status) {
                    com.uzbek.weatherapp.mvvm.base.Resource.Status.SUCCESS -> {
                        if (it.data != null) {

                            it.data.let { response ->
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
                                    description = response.weather.last().description.capitalize(),
                                    wind_speed = response.wind.speed,
                                    wind_deg = response.wind.deg,
                                    date = response.dt.toString(),
                                    city = response.name
                                )

                                mainViewModel.saveToDatabase(weather)

                                if (fromDialog){
                                    progressBtn.buttonDoneState()
                                    Handler().postDelayed({
                                        binding.loadingLayout.visibility = View.GONE
                                        binding.networkTxt.visibility = View.GONE
                                        binding.mainBack.visibility = View.VISIBLE
                                        uiListener.onUpdateUi(weather)
                                    },2500)


                                }else{
                                    pullRefreshView?.setRefreshing(false)

                                    uiListener.onUpdateUi(weather)

                                }
                                fromDialog = false
                            }
                        }
                    }

                    com.uzbek.weatherapp.mvvm.base.Resource.Status.ERROR -> {
                        pullRefreshView?.setRefreshing(false)

                        binding.mainBack.visibility = View.GONE
                        binding.loadingLayout.visibility = View.VISIBLE
                        binding.networkTxt.visibility = View.VISIBLE


                        progressBtn.initState()

                        if (fromDialog){
                            progressBtn.errorState()
                        }

                        binding.progressL.setOnClickListener {
                            fromDialog = true
                            progressBtn.buttonPressed()
                            Handler().postDelayed({
                                requestWeather(requestMode)
                            },1500)

                        }
                    }

                    com.uzbek.weatherapp.mvvm.base.Resource.Status.LOADING -> {

                    }
                }
            }

        }
    }


    @SuppressLint("SetTextI18n")
    private fun updateUi(weather: WeatherData) {
        binding.apply {

            val date = SimpleDateFormat(
                "dd/MM/yyyy hh:mm a",
                Locale.ENGLISH
            ).format(Date(weather.date.toLong() * 1000))

            val sunriseTime = SimpleDateFormat(
                "hh:mm a",
                Locale.ENGLISH
            ).format(Date(weather.sunrise.toLong() * 1000))

            val sunsetTime = SimpleDateFormat(
                "hh:mm a",
                Locale.ENGLISH
            ).format(Date(weather.sunset.toLong() * 1000))

            //Converting kelvin to celsius is easy: Just subtract 273.15
            val tempNum = (weather.temp - 273.15).roundToInt()


            address.text = "${weather.city},${weather.country}"
            updatedAt.text = "Sana: $date"
            temp.text = "${tempNum}°C"
            sunrise.text = sunriseTime.toString()
            sunset.text = sunsetTime.toString()
            wind.text = "${weather.wind_speed} km/h"
            pressure.text = weather.pressure.toString()
            humidity.text = "${weather.humidity}%"
        }

        val animation1: Animation = AnimationUtils.loadAnimation(
            requireActivity(),
            R.anim.cloud_move
        )
        val animation4: Animation = AnimationUtils.loadAnimation(
            requireActivity(),
            R.anim.cloud2
        )


        weather.description.let {

            if (it.lowercase().contains("rain") || it == "Thunderstorm"
            ) {
                binding.mainBack.setBackgroundResource(R.drawable.bg_rain)
                binding.WeatherImage.setImageResource(R.drawable.rain)
                binding.WeatherImage.visibility = View.GONE
                binding.WeatherImageLottie.visibility = View.VISIBLE
                binding.updatedAt.setTextColor(Color.GRAY)
                binding.WeatherImage.tag = R.drawable.rain
                binding.vintage4.visibility = View.VISIBLE
                binding.vintageo2.visibility = View.VISIBLE
                binding.vintage4.startAnimation(animation1)
                binding.vintage.startAnimation(animation1)
                binding.vintageo.startAnimation(animation4)
                binding.status.text = "Yomg'ir"



            } else if (it.lowercase().contains("snow")) {

                binding.apply {
                    mainBack.setBackgroundResource(R.drawable.bg_snow)
                    WeatherImage.setImageResource(R.drawable.snowflake)
                    binding.updatedAt.setTextColor(Color.GRAY)
                    binding.WeatherImage.visibility = View.VISIBLE
                    binding.WeatherImageLottie.visibility = View.GONE
                    WeatherImage.tag = R.drawable.snowflake
                    vintage4.visibility = View.VISIBLE
                    vintageo2.visibility = View.VISIBLE
                    binding.vintage4.startAnimation(animation1)
                    binding.vintage.startAnimation(animation1)
                    binding.vintageo.startAnimation(animation4)
                    binding.status.text = "Qor"

                }


            } else if (it == "Smoke" || it == "Mist"
                || it.lowercase().contains("clouds")) {

                binding.apply {
                    mainBack.setBackgroundResource(R.drawable.bg_snow)
                    WeatherImage.setImageResource(R.drawable.clouds)
                    binding.updatedAt.setTextColor(Color.GRAY)
                    binding.WeatherImage.visibility = View.VISIBLE
                    binding.WeatherImageLottie.visibility = View.GONE
                    WeatherImage.tag = R.drawable.clouds
                    vintage4.visibility = View.VISIBLE
                    vintageo2.visibility = View.VISIBLE
                    binding.vintage4.startAnimation(animation1)
                    binding.vintage.startAnimation(animation1)
                    binding.vintageo.startAnimation(animation4)
                    binding.status.text = "Tuman"
                }


            } else if (it == "Clear sky") {
                binding.apply {
                    mainBack.setBackgroundResource(R.drawable.bg_sun)
                    WeatherImage.tag = 1
                    WeatherImage.setImageResource(R.drawable.sunny)
                    binding.updatedAt.setTextColor(Color.WHITE)
                    binding.WeatherImage.visibility = View.VISIBLE
                    binding.WeatherImageLottie.visibility = View.GONE
                    vintage.visibility = View.GONE
                    vintage2.visibility = View.GONE
                    vintageo.visibility = View.GONE
                    vintage4.visibility = View.GONE
                    vintageo2.visibility = View.GONE
                    binding.status.text = "Musaffo"
                }
            } else {
                binding.apply {
                    mainBack.setBackgroundResource(R.drawable.bg_sun)
                    WeatherImage.tag = 1
                    WeatherImage.setImageResource(R.drawable.sunny)
                    binding.updatedAt.setTextColor(Color.WHITE)
                    binding.WeatherImage.visibility = View.VISIBLE
                    binding.WeatherImageLottie.visibility = View.GONE
                    vintage.visibility = View.VISIBLE
                    vintage2.visibility = View.VISIBLE
                    vintageo.visibility = View.VISIBLE
                    vintage4.visibility = View.GONE
                    vintageo2.visibility = View.GONE
                    binding.vintage.startAnimation(animation1)
                    binding.vintageo.startAnimation(animation4)
                    binding.status.text = ""
                }

            }

            binding.WeatherImage.startAnimation(animation3)
            binding.WeatherImage.setOnClickListener {
                binding.WeatherImage.startAnimation(animation3)
            }

        }




    }

    @ExperimentalCoroutinesApi
    private fun setupEverything() {




        if (appRepository.isFirstLaunch()) {
            if (App.isConnectedToInternet(requireContext())) {
                requestWeather(requestMode)
            }

        } else {

            if (App.isConnectedToInternet(requireContext())) {
                requestWeather(requestMode)

            } else {
                val progressBtn = ProgressButton(requireContext(), binding.progressL)


                mainViewModel.getLastWeather().observe(viewLifecycleOwner) {
                    it.let {

                        if (it != null) {
                            uiListener.onUpdateUi(it)
                        } else {
                            binding.mainBack.visibility = View.GONE
                            binding.loadingLayout.visibility = View.VISIBLE
                            binding.networkTxt.visibility = View.VISIBLE


                            progressBtn.initState()

                            if (fromDialog){
                                progressBtn.errorState()
                            }

                            binding.progressL.setOnClickListener {
                                fromDialog = true
                                progressBtn.buttonPressed()
                                Handler().postDelayed({
                                    requestWeather(requestMode)
                                },1500)

                            }

                            Toast.makeText(
                                requireContext(),
                                "No weather was found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }


    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_nav, menu)
        val item = menu.findItem(R.id.switcher)
        item.setActionView(R.layout.switch_layout)
        val mySwitch = item.actionView?.findViewById<SwitchCompat>(R.id.switchForActionBar)
        binding.apply {

            mySwitch?.setOnCheckedChangeListener { p0, isChecked ->

                if (mySwitch.isChecked) {
                    Toast.makeText(requireContext(), "Dark Mode", Toast.LENGTH_LONG).show()

                    box1.setBackgroundColor(Color.parseColor("#3C242424"))
                    box2.setBackgroundColor(Color.parseColor("#3C242424"))
                    box3.setBackgroundColor(Color.parseColor("#3C242424"))
                    box4.setBackgroundColor(Color.parseColor("#3C242424"))
                    box5.setBackgroundColor(Color.parseColor("#3C242424"))
                    infoWeather.setBackgroundColor(Color.parseColor("#86242424"))
                    mainBack.setBackgroundResource(R.drawable.dark_theme)
                    if (status.text == "Rain"
                        || status.text == "Shower rain" || status.text == "Light intensity shower rain"
                    ) {
                        WeatherImage.setImageResource(R.drawable.rain)


                    } else if (status.text == "Snow") {
                        WeatherImage.setImageResource(R.drawable.snowflake)


                    } else if (status.text == "Smoke" || status.text == "Broken clouds" || status.text == "Scattered clouds" || status.text == "Mist") {
                        WeatherImage.setImageResource(R.drawable.clouds)


                    } else if (status.text == "Overcast clouds" || status.text == "Clear sky") {
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

                    if (status.text == "Rain"
                        || status.text == "Shower rain" || status.text == "Light intensity shower rain"
                    ) {
                        mainBack.setBackgroundResource(R.drawable.bg_rain)
                        WeatherImage.setImageResource(R.drawable.rain)


                    } else if (status.text == "Snow") {
                        mainBack.setBackgroundResource(R.drawable.bg_snow)
                        WeatherImage.setImageResource(R.drawable.snowflake)


                    } else if (status.text == "Smoke" || status.text == "Broken clouds" || status.text == "Scattered clouds" || status.text == "Mist") {
                        mainBack.setBackgroundResource(R.drawable.bg_rain)
                        WeatherImage.setImageResource(R.drawable.clouds)


                    } else if (status.text == "Overcast clouds" || status.text == "Clear sky") {
                        mainBack.setBackgroundResource(R.drawable.bg_sun)
                        WeatherImage.setImageResource(R.drawable.sunny)
                    }
                }
            }

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.shareMenu -> {
                try {
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Weather Info")
                    var shareMessage = "\nLet`s try this Weather application:\n\n"
                    shareMessage =
                        shareMessage + "https://play.google.com/store/apps/details?id=" + com.uzbek.weatherapp.BuildConfig.APPLICATION_ID + "\n\n"
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                    startActivity(Intent.createChooser(shareIntent, "Share with "))
                } catch (e: Exception) {
                    Log.d("MainActivity", "Couldn` t load the web site")
                }
            }

            R.id.exitMenu -> {
                android.os.Process.killProcess(android.os.Process.myPid())
                exitProcess(1)
            }
            R.id.More -> {
                Toast.makeText(requireContext(), "Google Play Opened...", Toast.LENGTH_LONG).show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!weatherJob.isCancelled) {
            weatherJob.cancel()
        }
        isDestroyed = true
    }

    companion object {
        private const val TAG = "ThirdFragment"
    }


}