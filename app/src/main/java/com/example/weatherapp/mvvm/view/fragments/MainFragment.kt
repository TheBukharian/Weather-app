package com.example.weatherapp.mvvm.view.fragments

import android.os.Bundle
import android.view.*
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherapp.R
import com.example.weatherapp.databinding.MainFragmentBinding
import com.example.weatherapp.mvvm.adapters.ViewPagerAdapter
import com.example.weatherapp.mvvm.extensions.viewLifecycleLazy
import com.google.android.material.tabs.TabLayoutMediator
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView


open class MainFragment : BaseFragment(R.layout.main_fragment) {

    private val binding by viewLifecycleLazy { MainFragmentBinding.bind(requireView()) }

    private lateinit var demoCollectionAdapter: ViewPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {

            demoCollectionAdapter = ViewPagerAdapter(this@MainFragment)
            pager.adapter = demoCollectionAdapter


            TabLayoutMediator(tabLayout, pager) { tab, position ->
                val v: View = LayoutInflater.from(context).inflate(R.layout.tab_item_layout, null)

                val ItemImage = v.findViewById(R.id.imgText) as ImageView
                val ItemText = v.findViewById(R.id.cardText) as TextView

                when(position){
                    0->{
                        ItemImage.setImageResource(R.drawable.daily)
                        ItemText.text = "Today"
                    }

                    1->{
                        ItemImage.setImageResource(R.drawable.weekly)
                        ItemText.text = "Week"

                    }
                }


                tab.customView = v


            }.attach()
        }

    }

}