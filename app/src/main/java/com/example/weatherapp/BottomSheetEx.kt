package com.example.weatherapp


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetEx : BottomSheetDialogFragment()  {

    private var mBottomSheetListener:BottomSheetListener?=null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v =inflater.inflate(R.layout.fragment_bottom_sheet_ex, container, false)
        return v


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val regions= arrayOf(
            "Samarkand,UZ",
            "Bukhara,UZ",
            "London,GB",
            "Paris,FR",
            "Milan,IT",
            "New York City,US",
            "Moscow,RU",
            "Tashkent,UZ",
            "Hong Kong,HK",
            "Navoiy,UZ")


        val regAdapter: ArrayAdapter<String> =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                regions)

        btmList.adapter=regAdapter





        btmSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextChange(newText: String?): Boolean {
                regAdapter.filter.filter(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })


        btmList.setOnItemClickListener { parent, _, position, id ->
            val itemText=parent.getItemAtPosition(position)as String
            val adr = activity?.findViewById<TextView>(R.id.address)



            val intent= Intent(context,MainActivity::class.java)
            intent.putExtra(EXTRA_CITY,itemText)
            intent.flags =Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

            if (adr != null) {
                adr.text=itemText
            }




            Toast.makeText(requireContext(),"${adr?.text} SELECTED",Toast.LENGTH_LONG).show()

        }
    }




    interface BottomSheetListener{
        fun onOptionClick(text:String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            mBottomSheetListener =context as BottomSheetListener?

        }
        catch (e:ClassCastException){
            throw ClassCastException(requireContext().toString())
        }
    }




}
