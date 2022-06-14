package com.example.simulator.domain

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import android.os.Parcelable.Creator
import com.example.simulator.domain.Place

class Place protected constructor(`in`: Parcel) : Parcelable {
    @SerializedName("nome")
    var name: String?

    @SerializedName("imagem")
    var image: String?
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        val CREATOR: Creator<Place> = object : Creator<Place?> {
            override fun createFromParcel(`in`: Parcel): Place? {
                return Place(`in`)
            }

            override fun newArray(size: Int): Array<Place?> {
                return arrayOfNulls(size)
            }
        }
    }

    init {
        name = `in`.readString()
        image = `in`.readString()
    }
}