package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        textSwitcher.setFactory{
            val inflater=LayoutInflater.from(this@MenuActivity)
            inflater.inflate(R.layout.layout_title,null) as TextView
        }



        val  `in`= AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left)
        val  out= AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right)

        textSwitcher.inAnimation=`in`
        textSwitcher.outAnimation=out
    }
}
