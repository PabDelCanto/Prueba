package org.bedu.bedushop

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


class PagoExitosoFragment : Fragment() {

    private  var carritoFragment= CarritoFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainApp.listaCarritoHolder?.clear()
        MainApp.listaCarritoHolderId?.clear()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pago_exitoso, container, false)
        val button = view.findViewById<Button>(R.id.btnFinal)
        button.setOnClickListener {
            replaceFragment(carritoFragment, null)
            (activity as Shop).showBottomNav()
        }



        return view
    }
    private fun replaceFragment(fragment: Fragment, bundle:Bundle?){
        fragment.arguments = bundle//Enviamos Bundle, de existir
        val trans = requireActivity().supportFragmentManager.beginTransaction()
        trans.replace(R.id.fragemento_contenedor, fragment)
        trans.addToBackStack(null)
        trans.commit()
    }



}