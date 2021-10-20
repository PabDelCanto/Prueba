package org.bedu.bedushop

import android.os.Parcel
import android.os.Parcelable

data class rating (val rate:Double, val count:Int) :Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(rate)
        parcel.writeInt(count)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<rating> {
        override fun createFromParcel(parcel: Parcel): rating {
            return rating(parcel)
        }

        override fun newArray(size: Int): Array<rating?> {
            return arrayOfNulls(size)
        }
    }
}