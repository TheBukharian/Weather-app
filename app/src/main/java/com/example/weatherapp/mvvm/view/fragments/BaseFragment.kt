package com.example.weatherapp.mvvm.view.fragments

import android.Manifest
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.weatherapp.R
import com.example.weatherapp.mvvm.repositories.AppRepository
import com.example.weatherapp.mvvm.utilities.AppDataStore
import com.example.weatherapp.mvvm.view.activities.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
open class BaseFragment(layoutId: Int) : Fragment(layoutId) {

    @Inject
    lateinit var appRepository: AppRepository

    @Inject
    lateinit var appDataStore: AppDataStore




    fun requestPermissions(resultCallback: ActivityResultLauncher<Array<String>>) {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) else arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        resultCallback.launch(
            permission
        )
    }
    fun initToolbar(toolbar: Toolbar) {

        val appCompatActivity = requireActivity() as AppCompatActivity
        toolbar.title = ""
        appCompatActivity.setSupportActionBar(toolbar)
//        toolbar.findViewById<View>(R.id.done).setOnClickListener { requireActivity().onBackPressed() }
    }

    companion object{
        lateinit var dialog: AlertDialog


        fun showLoading(cancelable: Boolean, activity: MainActivity) {

            val layoutInflater = LayoutInflater.from(activity.applicationContext)
            val view = layoutInflater.inflate(R.layout.loader, null)
            val progressDialog = AlertDialog.Builder(activity)
            progressDialog.setView(view)
            progressDialog.setCancelable(cancelable)

            dialog = progressDialog.create()
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window?.setBackgroundDrawable(ColorDrawable())
            dialog.show()
        }

        fun hideLoading() {
            if (this::dialog.isInitialized) {
                dialog.dismiss()
            }
        }
    }
}