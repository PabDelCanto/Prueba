package org.bedu.bedushop

import android.os.Parcel
import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.json.JSONObject
import retrofit2.http.Url

open class Product:RealmObject(){
    @PrimaryKey
    var id: Int? =null

    var name: String? =null
    var price: Float?=null
    var description: String?=null
    var category: String? =null
    var rate: Float? = null
    var idImage: String? =null
    var numOpinions: String? =null




}


/*class Product (
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
}*/