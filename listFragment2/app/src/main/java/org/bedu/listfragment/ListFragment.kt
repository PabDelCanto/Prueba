package org.bedu.listfragment

import RecyclerAdapter
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_list.*
import java.io.IOException


class ListFragment: Fragment() {

    private lateinit var mAdapter : RecyclerAdapter
    private var listener : (Product) ->Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpRecyclerView()
    }

    fun setListener(l: (Product) ->Unit){
        listener = l
    }

    //configuramos lo necesario para desplegar el RecyclerView
    private fun setUpRecyclerView(){
        recyclerProducts.setHasFixedSize(true)
        recyclerProducts.layoutManager = LinearLayoutManager(activity)
        //seteando el Adapter
        mAdapter = RecyclerAdapter( requireActivity(), getProducts(requireContext()), listener)
        //asignando el Adapter al RecyclerView
        recyclerProducts.adapter = mAdapter
    }

    private fun getJsonDataFromAsset(context: Context, fileName: String = "products.json"): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    fun getProducts(context: Context): MutableList<Product>{
        val jsonString = getJsonDataFromAsset(context)
        val listProductType = object : TypeToken<MutableList<Product>>(){}.type
        return Gson().fromJson(jsonString, listProductType)
    }
}