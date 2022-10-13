package com.uzbek.weatherapp.mvvm.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentSecondBinding
import com.example.weatherapp.databinding.MainFragmentBinding
import com.example.weatherapp.mvvm.extensions.viewLifecycleLazy


class SecondFragment : BaseFragment(R.layout.fragment_second) {

    private val binding by viewLifecycleLazy { FragmentSecondBinding.bind(requireView()) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }



}