package org.bedu.bedushop

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import io.realm.Realm
import org.bedu.bedushop.Product

class DetailFragment: Fragment() {
    private lateinit var tvProduct: TextView
    private lateinit var tvDescription: TextView
    private lateinit var rbRate: RatingBar
    private lateinit var imgProduct: ImageView
    private lateinit var tvPrice: TextView
    private lateinit var tvCuotas : TextView
    private lateinit var tvNumOpinions : TextView




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)



        // Datos que se cargan al View del Detalle
        tvProduct = view.findViewById(R.id.tvProduct)
        tvDescription = view.findViewById(R.id.tvDescription)
        rbRate = view.findViewById(R.id.rbRate)
        imgProduct = view.findViewById(R.id.imgProduct)
        tvPrice = view.findViewById(R.id.tvPrice)
        tvCuotas = view.findViewById(R.id.tvCuotas)
        tvNumOpinions = view.findViewById(R.id.tvNumOpinions)


        //Funcion que te escrolea SOLO EL DETALLE
        tvDescription.movementMethod = ScrollingMovementMethod()


        return view
    }

    // Funcion que carga los datos del producto en la pantalla del Detalle
    fun showProduct(product: ProductoApi){

        view?.visibility = View.VISIBLE
        tvProduct.text = product.title
        tvDescription.text = product.description
        rbRate.rating = product.rating.rate.toFloat()
        Picasso.with(requireContext()).load(product.image).into(imgProduct)
        tvPrice.text = "$${product.price}"
        tvCuotas.text = "$%.2f".format(product.price.toFloat()/6f)
        tvNumOpinions.text = product.rating.count.toString()

    }

}


