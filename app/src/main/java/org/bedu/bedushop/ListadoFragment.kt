package org.bedu.bedushop

import RecyclerAdapter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_listado.*
import kotlinx.android.synthetic.main.fragment_listado.*
import kotlinx.android.synthetic.main.fragment_recycler.*
import java.io.IOException


class ListadoFragment : Fragment() {

    private lateinit var mAdapter : RecyclerAdapter
    private var listener : (ProductoApi) ->Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout de fragment
        return inflater.inflate(R.layout.fragment_recycler, container,false) //Apunta al fragmento que tiene el recyclerView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpRecyclerView()//Aquí abriría otro thread?
    }

    fun setListener(l: (ProductoApi) ->Unit){
        listener = l
    }

    //configuramos lo necesario para desplegar el RecyclerView
    private fun setUpRecyclerView() {
        var listStr = this.arguments?.getString(SHOP_LIST)
        val listProductType = object : TypeToken<MutableList<ProductoApi>>(){}.type
        val prods = Gson().fromJson<MutableList<ProductoApi>>(MainApp.array, listProductType)
        //Log.d("setUpRecycler", prods.toString())
        recyclerProducts.setHasFixedSize(true)
        recyclerProducts.layoutManager = LinearLayoutManager(activity)
        //seteando el Adapter
        mAdapter = RecyclerAdapter(requireActivity(), prods, listener)
        //asignando el Adapter al RecyclerView
        recyclerProducts.adapter = mAdapter
    }
}




