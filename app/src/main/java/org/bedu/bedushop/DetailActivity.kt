package org.bedu.bedushop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import io.realm.Realm
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.bedu.bedushop.Product

class DetailActivity : AppCompatActivity() {
    companion object {
        val PRODUCT = "PRODUCT"
    }
    private lateinit var addBoton: Button
    private  var carritoFragment= CarritoFragment()
    private lateinit var carroCompra: LottieAnimationView
    private lateinit var products: List<ProductoApi>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

       carroCompra=findViewById(R.id.imgProduct)

        //!! Transition sin terminar para el detail
        val transition = Slide(Gravity.BOTTOM).apply {
            duration = 500
            excludeTarget(window.decorView.findViewById<View>(R.id.action_bar_container), true)
            excludeTarget(android.R.id.statusBarBackground, true)
            excludeTarget(android.R.id.navigationBarBackground, true)
        }
        window.enterTransition = transition
        //!! Transition sin terminar para el detail

        //Se realiza el binding de la informacion recibida desde el lsit fragement  al detail fragment

        val product = intent.getParcelableExtra<ProductoApi>(PRODUCT)
        val detailFragment = supportFragmentManager.findFragmentById(R.id.fragmentDetail) as? DetailFragment
        if (product != null) {
            detailFragment?.showProduct(product)


        //Al seleccionar el boton de "Agregar al Carrito " se redirecciona al Activity de Shop, ingresando al fragmente de Carrito
        addBoton = findViewById(R.id.addCarrito)
            addBoton.setOnClickListener{
                runBlocking{
                    animarCarrito(carroCompra,R.raw.carrito)
                    delay(200)
                }

                val intentDetail = Intent(this, Shop::class.java).apply {
                    //DATOS EXTRA
                    putExtra("origen", "DETAIL")
                    putExtra(Intent.EXTRA_INDEX, product)
                };

                startActivity(intentDetail)


            }
        }

    }

}
    fun animarCarrito (imageView: LottieAnimationView, animation:Int){
        imageView.setAnimation(animation)
        imageView.speed=0.2f
        imageView.playAnimation()
    }

