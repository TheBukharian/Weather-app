package com.uzbek.weatherapp.mvvm.view.fragments

import android.os.Bundle
import android.view.*
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherapp.R
import com.example.weatherapp.databinding.MainFragmentBinding
import com.uzbek.weatherapp.mvvm.adapters.ViewPagerAdapter
import com.example.weatherapp.mvvm.extensions.viewLifecycleLazy
import com.google.android.material.tabs.TabLayoutMediator
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.uzbek.weatherapp.mvvm.utilities.pulltorefresh.PullToRefreshView


open class MainFragment : BaseFragment(R.layout.main_fragment) {

    private val binding by viewLifecycleLazy { MainFragmentBinding.bind(requireView()) }

    private lateinit var demoCollectionAdapter: com.uzbek.weatherapp.mvvm.adapters.ViewPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {

//            demoCollectionAdapter = ViewPagerAdapter(this@MainFragment)
//            pager.adapter = demoCollectionAdapter
//
//
//            TabLayoutMediator(tabLayout, pager) { tab, position ->
//                val v: View = LayoutInflater.from(context).inflate(R.layout.tab_item_layout, null)
//
//                val ItemImage = v.findViewById(R.id.imgText) as ImageView
//                val ItemText = v.findViewById(R.id.cardText) as TextView
//
//                when(position){
//                    0->{
//                        ItemImage.setImageResource(R.drawable.daily)
//                        ItemText.text = "Today"
//                    }
//                }
//
//
//                tab.customView = v


//            }.attach()
        }

    }

}