package org.bedu.bedushop

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView


class ResumenPagoFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val pago = PagoExitosoFragment()
        val view = inflater.inflate(R.layout.fragment_resumen_pago, container, false)
        val button = view.findViewById<Button>(R.id.btnPagar)
        val subtotalTv = view.findViewById<TextView>(R.id.subtotalTv)
        val totalTv = view.findViewById<TextView>(R.id.totalTv)

        subtotalTv.text = calcularSubtotal().toString()
        totalTv.text = "$%.2f".format(calcularSubtotal() + 30f)

        button.setOnClickListener {
            replaceFragment(pago, null)
            (activity as Shop).compraNotification()
        }
        super.onViewCreated(view, savedInstanceState)
        (activity as Shop).hideBottomNav()


        return view
    }
    private fun replaceFragment(fragment: Fragment, bundle:Bundle?){
        fragment.arguments = bundle//Enviamos Bundle, de existir
        val trans = requireActivity().supportFragmentManager.beginTransaction()
        trans.replace(R.id.fragemento_contenedor, fragment)
        trans.addToBackStack(null)
        trans.commit()
    }
    override fun onDetach() {
        super.onDetach()
        (activity as Shop).showBottomNav()
    }

    private fun calcularSubtotal():Float{
        var subtotal : Float  = 0f
        MainApp.listaCarritoHolder?.forEach{
            subtotal += it.stock * it.price
        }
        return subtotal
    }

}