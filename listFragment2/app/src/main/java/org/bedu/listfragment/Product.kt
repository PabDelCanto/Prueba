package org.bedu.listfragment

import android.os.Parcel
import android.os.Parcelable

class Product (
    val name: String,
    val description: String,
    val price: String,
    val rating: Float,
    val idImage: String,
    val numOpinions: Int
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readFloat()!!,
        parcel.readString()!!,
        parcel.readInt()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(price)
        parcel.writeFloat(rating)
        parcel.writeString(idImage)
        parcel.writeInt(numOpinions)
    }

    override fun describeContents(): Int {
        return 0
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