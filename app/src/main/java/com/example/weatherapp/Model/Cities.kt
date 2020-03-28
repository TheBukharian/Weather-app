package com.example.weatherapp.Model

import android.os.Parcel
import android.os.Parcelable

class Cities(var City: String?):Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()) {}

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(City)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Cities> {
        override fun createFromParcel(parcel: Parcel): Cities {
            return Cities(parcel)
        }

        override fun newArray(size: Int): Array<Cities?> {
            return arrayOfNulls(size)
        }
    }
}