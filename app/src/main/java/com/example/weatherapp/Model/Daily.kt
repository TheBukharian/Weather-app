package com.example.weatherapp.Model

 class Daily(val Days:String,val weatherImage:String, val Degrees:String) {

     override fun toString(): String {
         return Days
     }
}