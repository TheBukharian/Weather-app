package com.example.weatherapp


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherapp.MainActivity.weatherTask
import com.example.weatherapp.Model.Cities
import com.example.weatherapp.Utility.EXTRA_CITY

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_bottom_sheet_ex.view.*


class BottomSheetEx : BottomSheetDialogFragment()  {
    private var mBottomSheetListener:BottomSheetListener?=null


    var CITY=Cities("Tashkent,UZ")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v =inflater.inflate(R.layout.fragment_bottom_sheet_ex, container, false)
        val mainObj= MainActivity()


        v.bukhara.setOnClickListener { CITY.City="Bukhara,UZ"
            Log.d("BottomSheetEx",CITY.City)
//            v.putExtra(EXTRA_CITY,CITY)
            println(CITY.City)

        }

        v.samarkand.setOnClickListener {CITY.City="Samarqand,UZ"
            Log.d("BottomSheetEx",CITY.City)
            println(CITY.City)
        }


        v.navai.setOnClickListener { CITY.City="Navoiy,UZ"
            Log.d("BottomSheetEx",CITY.City)
            println(CITY.City)
        }


        v.london.setOnClickListener { CITY.City="London,GB"
            Log.d("BottomSheetEx",CITY.City)
            println(CITY.City)
        }


        v.paris.setOnClickListener { CITY.City="Paris,FR"
            Log.d("BottomSheetEx",CITY.City)
            println(CITY.City)
        }


        v.milan.setOnClickListener { CITY.City="Milan,IT"
            Log.d("BottomSheetEx",CITY.City)
            println(CITY.City)
        }



        return v
    }

    interface BottomSheetListener{
        fun onOptionClick(text:String)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try{
            mBottomSheetListener =context as BottomSheetListener?
        }
        catch (e:ClassCastException){
            throw ClassCastException(context!!.toString())
        }
    }



}
