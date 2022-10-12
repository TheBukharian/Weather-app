package com.example.weatherapp.mvvm.view.activities


import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.startup.AppInitializer
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.mvvm.utilities.AppDataStore
import com.example.weatherapp.mvvm.view.fragments.BottomSheetExFragment
import dagger.hilt.android.AndroidEntryPoint
import net.danlew.android.joda.JodaTimeInitializer
import javax.inject.Inject


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



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }


    }

    override fun onOptionClick(text: String) {
        TODO("Not yet implemented")
    }


}

