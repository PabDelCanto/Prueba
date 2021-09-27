package org.bedu.listfragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter (
    private val context: Context,
    private val products: MutableList<Product>,
    private val clickListener : (Product) -> Unit) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_contact, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        val product = products.get(position)
        holder.bind(product, context)
        holder.view.setOnClickListener{clickListener}
    }

    override fun getItemCount(): Int {
        return products.size
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){

        val productName = view.findViewById(R.id.tvProduct) as TextView
        val description = view.findViewById(R.id.tvDescription) as TextView
        val price = view.findViewById(R.id.tvPrice) as TextView
        val image = view.findViewById(R.id.imgProduct) as ImageView


        fun bind(product:Product, context:Context){
            productName.text = product.name
            description.text = product.description
            price.text = "$${product.price}"
            image.setImageResource(product.idImage)
        }
    }

}