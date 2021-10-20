import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import io.realm.Realm
import org.bedu.bedushop.Opciones
import org.bedu.bedushop.Product
import org.bedu.bedushop.ProductoApi
import org.bedu.bedushop.R

//Declaración con constructor
class RecyclerAdapter(
    private val context: Context,
    private var products: MutableList<ProductoApi>,
    private val clickListener: (ProductoApi) -> Unit): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    //Aquí atamos el ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products?.get(position)
        if (product != null) {
            holder.bind(product, context)
        }

        holder.view.setOnClickListener {
            if (product != null) {
                clickListener(product)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_contact, parent, false))
    }

    override fun getItemCount(): Int {
        if (products != null) {
            return products.size
        }
        return 0
    }

    //El ViewHolder ata los datos del RecyclerView a la Vista para desplegar la información
    //También se encarga de gestionar los eventos de la View, como los clickListeners
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        //obteniendo las referencias a las Views
        val productName = view.findViewById(R.id.tvProduct) as TextView
        val ratingBar = view.findViewById(R.id.rbRate) as RatingBar
        val price = view.findViewById(R.id.tvPrice) as TextView
        val image = view.findViewById(R.id.imgProduct) as ImageView

        //"atando" los datos a las Views
        fun bind(product: ProductoApi, context: Context) {
            productName.text = product.title
            price.text = "$${product.price}"
            ratingBar.rating = product.rating.rate.toFloat()
            Picasso.with(context).load(product.image).into(image)
        }
    }
}