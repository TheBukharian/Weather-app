package com.example.weatherapp


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_bottom_sheet_ex.view.*


class BottomSheetEx : BottomSheetDialogFragment()  {
    private var mBottomSheetListener:BottomSheetListener?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v =inflater.inflate(R.layout.fragment_bottom_sheet_ex, container, false)
        val mainObj=MainActivity()

       v. shareBtn.setOnClickListener {

            //Share this app from other apps!!!!!!!!!
        }



        v.bukhara.setOnClickListener { mainObj.CITY="Bukhara,UZ"
            println(mainObj.CITY)
        }
        v.samarkand.setOnClickListener {mainObj.CITY="Samarqand,UZ"
            println(mainObj.CITY)
            }
        v.navai.setOnClickListener { mainObj.CITY="Navoiy,UZ"
            println(mainObj.CITY)
        }
        v.london.setOnClickListener { mainObj.CITY="London,GB"
            println(mainObj.CITY)
            }
        v.paris.setOnClickListener { mainObj.CITY="Paris,FR"
            println(mainObj.CITY)}
        v.milan.setOnClickListener { mainObj.CITY="Milan,IT"
            println(mainObj.CITY)}



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
