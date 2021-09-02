package com.example.weatherapp.mvvm.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.lottie.LottieAnimationView
import com.example.weatherapp.R

class ProgressButton(ct: Context, view: View) {
    private var progressBar: ProgressBar = view.findViewById(R.id.progressbar)
    private var textView: TextView = view.findViewById(R.id.loadingtext)
    private var layout:LinearLayout = view.findViewById(R.id.progressLayout)
    private var lottie: LottieAnimationView = view.findViewById(R.id.doneImg)
    private var relativeLayout:RelativeLayout = view.findViewById(R.id.progressL)

    private var context: Context = ct

    init {
        relativeLayout.isClickable = true

    }


    @SuppressLint("UseCompatLoadingForColorStateLists")
    fun buttonPressed(){

        lottie.visibility = View.GONE
        layout.visibility = View.VISIBLE
        relativeLayout.backgroundTintList = context.resources.getColorStateList(R.color.greenBtn)
        progressBar.visibility = View.VISIBLE
        textView.visibility = View.VISIBLE
        textView.text = "Please wait..."
    }

    fun buttonDoneState(){

        relativeLayout.isClickable = false
        layout.visibility = View.GONE
        Handler().postDelayed({
            lottie.visibility = View.VISIBLE
        }, 600)

    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    fun errorState(){
        lottie.visibility = View.GONE
        layout.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        textView.visibility = View.VISIBLE
        relativeLayout.backgroundTintList = context.resources.getColorStateList(R.color.redBtn)

        textView.text = "Try Again"
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    fun initState(){
        lottie.visibility = View.GONE
        layout.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        textView.visibility = View.VISIBLE
        relativeLayout.backgroundTintList = context.resources.getColorStateList(R.color.greenBtn)

        textView.text = "Try Again"
    }




}