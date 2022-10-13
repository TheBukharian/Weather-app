package com.uzbek.weatherapp.mvvm.view.fragments


import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.bottomsheetdialogfragment.viewBinding
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.mvvm.view.activities.MainActivity
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentBottomSheetExBinding
import com.example.weatherapp.databinding.MainFragmentBinding
import com.example.weatherapp.mvvm.extensions.navigateSafe
import com.example.weatherapp.mvvm.extensions.viewLifecycleLazy
import com.example.weatherapp.mvvm.repositories.AppRepository
import com.example.weatherapp.mvvm.viewmodel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


class BottomSheetExFragment : BottomSheetDialogFragment()  {

    private var mBottomSheetListener: BottomSheetListener?=null
    private val bottomBinding: FragmentBottomSheetExBinding by viewBinding()
    private val mainViewModel by activityViewModels<MainViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return bottomBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        bottomBinding.root.setBackgroundResource(R.color.transparent)

        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.ThemeOverlay_Demo_BottomSheetDialog)


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


        bottomBinding.apply {


            btmList.adapter=regAdapter
            btmList.isNestedScrollingEnabled = false

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
                val itemText = parent.getItemAtPosition(position) as String
                val adr = activity?.findViewById<TextView>(R.id.address)


                mainViewModel.setSelectedCityName(itemText.substringBefore(","))


                if (adr != null) {
                    adr.text=itemText
                }
                setFragmentResult("adBanner", bundleOf())

                BaseFragment.showLoading(false,requireActivity() as MainActivity)
                findNavController().navigateSafe(R.id.action_bottomSheetExFragment_to_thirdFragment)
            }
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
