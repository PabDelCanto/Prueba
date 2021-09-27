package org.bedu.listfragment

import android.os.Parcel
import android.os.Parcelable

class Product(
    val name: String,
    val description: String,
    val price: String,
    val rating: Float,
    val idImage: Int
) : Parcelable{
    constructor(parcel:Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readFloat()!!,
        parcel.readInt()!!
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p: Parcel?, p1: Int) {
        p?.writeString(name)
        p?.writeString(description)
        p?.writeString(price)
        p?.writeFloat(rating)
        p?.writeInt(idImage)
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}