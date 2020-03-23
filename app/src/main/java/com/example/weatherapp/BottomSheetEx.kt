package com.example.weatherapp


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherapp.MainActivity.weatherTask
import com.example.weatherapp.Model.Cities

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_bottom_sheet_ex.view.*


class BottomSheetEx : BottomSheetDialogFragment()  {
    private var mBottomSheetListener:BottomSheetListener?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v =inflater.inflate(R.layout.fragment_bottom_sheet_ex, container, true)
        val mainObj= MainActivity()


        v.bukhara.setOnClickListener { mainObj.CITY.City="Bukhara,UZ"
            Log.d("BottomSheetEx",mainObj.CITY.City)
            println(mainObj.CITY.City)
//            MainActivity.putExtra()
        }

        v.samarkand.setOnClickListener {mainObj.CITY.City="Samarqand,UZ"
            Log.d("BottomSheetEx",mainObj.CITY.City)
            println(mainObj.CITY.City)
        }


        v.navai.setOnClickListener { mainObj.CITY.City="Navoiy,UZ"
            Log.d("BottomSheetEx",mainObj.CITY.City)
            println(mainObj.CITY.City)
        }


        v.london.setOnClickListener { mainObj.CITY.City="London,GB"
            Log.d("BottomSheetEx",mainObj.CITY.City)
            println(mainObj.CITY.City)
        }


        v.paris.setOnClickListener { mainObj.CITY.City="Paris,FR"
            Log.d("BottomSheetEx",mainObj.CITY.City)
            println(mainObj.CITY.City)
        }


        v.milan.setOnClickListener { mainObj.CITY.City="Milan,IT"
            Log.d("BottomSheetEx",mainObj.CITY.City)
            println(mainObj.CITY.City)
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
