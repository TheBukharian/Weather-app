package com.uzbek.weatherapp.mvvm.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherapp.R
import com.example.weatherapp.mvvm.view.fragments.MainFragment
import com.example.weatherapp.mvvm.view.fragments.SecondFragment
import com.example.weatherapp.mvvm.view.fragments.ThirdFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {

        when (position) {
            0 -> return ThirdFragment()
            1 -> return SecondFragment()
        }

        return ThirdFragment()
    }



}

