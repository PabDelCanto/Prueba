package org.bedu.bedushop

import android.os.Parcel
import android.os.Parcelable

class ProductoApi (
    val id: Int,
    val title: String,
    val price: Float,
    val description: String,
    val rating: rating,
    val image: String,
    var stock: Int = 0

    ): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt()!!,
        parcel.readString()!!,
        parcel.readFloat()!!,
        parcel.readString()!!,
        parcel.readParcelable(org.bedu.bedushop.rating::class.java.classLoader)!!,
        parcel.readString()!!,
        parcel.readInt()

        ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeFloat(price)
        parcel.writeString(description)
        parcel.writeParcelable(rating, flags)
        parcel.writeString(image)
        parcel.writeInt(stock)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductoApi> {
        override fun createFromParcel(parcel: Parcel): ProductoApi {
            return ProductoApi(parcel)
        }

        override fun newArray(size: Int): Array<ProductoApi?> {
            return arrayOfNulls(size)
        }
    }
}