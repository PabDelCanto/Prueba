package org.bedu.bedushop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView


class RecyclerAdapterOpciones(val options : List<Opciones>, val listener: (Opciones) -> Unit) :
    RecyclerView.Adapter<RecyclerAdapterOpciones.ViewHolder>(){

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        private val opcion = view.findViewById<TextView>(R.id.tvOpcion)
        private val imagen = view.findViewById<ImageView>(R.id.imgOpcion)
        private val flecha = view.findViewById<ImageView>(R.id.flechita)

        fun bind(option: Opciones){
            opcion.text = option.opcion
            imagen.setImageResource(option.vector)
            flecha.setImageResource(option.flecha)

            itemView.setOnClickListener {
                if(opcion.text.toString() == "Mis direcciones"){
                   // Toast.makeText(itemView.context, "la opcion que elegiste es ${opcion.text}", Toast.LENGTH_SHORT).show()

                }
            }
        }




    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(options[position])
        holder.itemView.setOnClickListener{ listener(options[position])}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_perfil, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return options.size
    }


}