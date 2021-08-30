package com.example.weatherapp.mvvm.view.fragments

import android.os.Bundle
import android.view.*
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherapp.R
import com.example.weatherapp.databinding.MainFragmentBinding
import com.example.weatherapp.mvvm.adapters.ViewPagerAdapter
import com.example.weatherapp.mvvm.extensions.viewLifecycleLazy
import com.google.android.material.tabs.TabLayoutMediator



open class MainFragment : BaseFragment(R.layout.main_fragment) {

    private val binding by viewLifecycleLazy { MainFragmentBinding.bind(requireView()) }

    private lateinit var demoCollectionAdapter: ViewPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {

            demoCollectionAdapter = ViewPagerAdapter(this@MainFragment)
            pager.adapter = demoCollectionAdapter


            TabLayoutMediator(tabLayout, pager) { tab, position ->
                tab.text = "OBJECT ${(position + 1)}"
                tab.customView = layoutInflater.inflate(R.layout.tab_item_layout, null)

            }.attach()
        }

    }

}