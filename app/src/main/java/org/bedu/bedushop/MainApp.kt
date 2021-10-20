package org.bedu.bedushop

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import io.realm.Realm
import io.realm.RealmConfiguration
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainApp: Application() {
    companion object{
        var array : String = String()
        var listaCarritoHolder = CarritoHolder.instance?.listaCarrito
        var listaCarritoHolderId = CarritoHolderId.instance?.listaCarritoId
    }
    override fun onCreate(){
        super.onCreate()
        //Aca Tengo que ver como es lo de networking que habua visto pablo
         getProductsList()
    }


//Esta funcion esta relacionada con obtener los datos para el val array
    fun getJsonFile():String{
        return applicationContext
            .assets
            .open("products.json").bufferedReader().use { it.readText() }
    }

    private fun getProductsList(){
        var products: MutableList<ProductoApi> = mutableListOf()
        var json : String
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create<WebServices>(WebServices::class.java)
        service.getAllProducts().enqueue(object : Callback<MutableList<ProductoApi>>{
            override fun onResponse(
                call: Call<MutableList<ProductoApi>>,
                response: Response<MutableList<ProductoApi>>
            ) {
                products = response.body()!!
                Log.d("json?", products.toString())
                Log.i("GsonConverter", Gson().toJson(products))
                array = Gson().toJson(products)

            }

            override fun onFailure(call: Call<MutableList<ProductoApi>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

}
