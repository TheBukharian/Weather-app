package com.example.weatherapp.mvvm.view.activities


import android.content.Intent
import android.content.Intent.*
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.startup.AppInitializer
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.mvvm.adapters.ViewPagerAdapter
import com.example.weatherapp.mvvm.utilities.AppDataStore
import com.example.weatherapp.mvvm.utilities.Constants
import com.example.weatherapp.mvvm.view.fragments.BottomSheetExFragment
import com.example.weatherapp.mvvm.view.fragments.MainFragment
import com.google.android.gms.ads.MobileAds
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import net.danlew.android.joda.JodaTimeInitializer
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.system.exitProcess


@AndroidEntryPoint
open class MainActivity : AppCompatActivity(), BottomSheetExFragment.BottomSheetListener{

    @Inject
    lateinit var appDataStore: AppDataStore


    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setIcon(R.mipmap.ic_launcher)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setLogo(R.mipmap.ic_launcher)
        supportActionBar?.setDisplayUseLogoEnabled(true)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        AppInitializer.getInstance(this).initializeComponent(JodaTimeInitializer::class.java)
        MobileAds.initialize(this) {}



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }


    }

    override fun onOptionClick(text: String) {
        TODO("Not yet implemented")
    }


}

