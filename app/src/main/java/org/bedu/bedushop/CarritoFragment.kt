package org.bedu.bedushop

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_carrito.*


class CarritoFragment : Fragment() {

    private lateinit var mAdapter: RecyclerAdapterCarrito
    private lateinit var mpName: TextView
    private lateinit var mpimg: ImageView
    private lateinit var mpPrice: TextView


    //Esta es la lista de Productos que tenemos real en el carrito, por sus id
    private var products = MainApp.listaCarritoHolder
    private var idProducts = MainApp.listaCarritoHolderId


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val resumen = ResumenPagoFragment()
        val view = inflater.inflate(R.layout.fragment_carrito, container, false)

        //Funcion para pasar al fragment de compra
        val boton = view.findViewById<Button>(R.id.buttonComprar)
        boton.setOnClickListener {
            replaceFragment(resumen, null)
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        idProducts!!.add(0)
        if (arguments?.isEmpty != true && arguments?.getParcelable<ProductoApi>(SHOP_PRODUCT)!=null) {
            var bundleDesdeDetail = arguments?.getParcelable<ProductoApi>(SHOP_PRODUCT)!! //No hay persistencia en el array!

            //Si no fallo mim bundle, y por lo tanto no es nulo
            if (bundleDesdeDetail.id != null) {
                    if (bundleDesdeDetail.id in idProducts!!) {
                        Log.d("ENTRA EN IF", "OK")
                        products!!.forEach {
                            if(it.id == bundleDesdeDetail.id){
                                it.stock++
                            }
                        }
                        Log.d("Aumentar Stock", bundleDesdeDetail.stock.toString())
                    } else {
                        products!!.add(bundleDesdeDetail)
                        products!!.forEach {
                            if(it.id == bundleDesdeDetail.id){
                                it.stock++
                            }
                        }
                        idProducts!!.add(bundleDesdeDetail.id)
                        Log.d("crear", "itemnuevo")
                        Log.d("Item Id", bundleDesdeDetail.id.toString())
                        Log.d("Lista Id", idProducts.toString())
                    }
               arguments?.clear()
                setUpRecyclerView(products!!)
            }
        }
        setUpRecyclerView(products!!)
    }

private fun replaceFragment(fragment: Fragment, bundle: Bundle?) {
fragment.arguments = bundle//Enviamos Bundle, de existir
val trans = requireActivity().supportFragmentManager.beginTransaction()
trans.replace(R.id.fragemento_contenedor, fragment)
trans.addToBackStack(null)
trans.commit()
}


private fun setUpRecyclerView(listadoProductosCarrito : MutableList<ProductoApi>) {
recyclerCarrito.setHasFixedSize(true)
recyclerCarrito.layoutManager = LinearLayoutManager(activity)
mAdapter = RecyclerAdapterCarrito(requireActivity(), listadoProductosCarrito)//esto es uma prueba
//asignando el Adapter al RecyclerView
recyclerCarrito.adapter = mAdapter
}
}
