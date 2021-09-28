package org.bedu.listfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import coil.api.load
import com.squareup.picasso.Picasso
import kotlin.math.roundToLong
import android.text.method.ScrollingMovementMethod




class DetailFragment : Fragment() {
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

        tvProduct = view.findViewById(R.id.tvProduct)
        tvDescription = view.findViewById(R.id.tvDescription)
        rbRate = view.findViewById(R.id.rbRate)
        imgProduct = view.findViewById(R.id.imgProduct)
        tvPrice = view.findViewById(R.id.tvPrice)
        tvCuotas = view.findViewById(R.id.tvCuotas)
        tvNumOpinions = view.findViewById(R.id.tvNumOpinions)

        tvDescription.movementMethod = ScrollingMovementMethod()


        return view
    }

    fun showProduct(product: Product){

        view?.visibility = View.VISIBLE
        tvProduct.text = product.name
        tvDescription.text = product.description
        rbRate.rating = product.rating
        Picasso.with(requireContext()).load(product.idImage).into(imgProduct)
        tvPrice.text = "$${product.price}"
        tvCuotas.text = "$%.2f".format(product.price.toFloat()/6f)
        tvNumOpinions.text = product.numOpinions.toString()

    }

}