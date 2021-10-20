package org.bedu.bedushop

import android.Manifest
import android.animation.AnimatorInflater
import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import androidx.fragment.app.Fragment
//import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.transition.TransitionInflater
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.gson.Gson
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_shop.*
import kotlinx.android.synthetic.main.recycler_perfil.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

//Asignamos nuevas variables globales
const val USER_EMAIL_SHOP = "org.bedu.bedushop.USER_EMAIL_SHOP"
const val USER_FULL_NAME_SHOP = "org.bedu.bedushop.USER_FULL_NAME_SHOP"
const val USER_AVATAR_SHOP = "org.bedu.bedushop.USER_AVATAR_SHOP"
const val SHOP_LIST = "org.bedu.bedushop.SHOP_LIST"
const val PRODUCTO_ID = "org.bedu.bedushop.PRODUCTO_ID"
val CHANNEL_OTHERS = "OTROS"
const val SHOP_PRODUCT = "org.bedu.bedushop.SHOP_PRODUCT"

//PREFERENCIAS
val PREFS_NAME = "org.bedu.bedushop"
val NAME = "NAME"
val MAIL = "MAIL"
val AVATAR = "AVATAR"

class Shop : AppCompatActivity() {
    private val compraFragment = ResumenPagoFragment()
    private  var usuarioFragment= UsuarioFragment()
    private  var listaFragment= ListadoFragment()
    private  var carritoFragment= CarritoFragment()
    private lateinit var menuSuperior: Menu
    private lateinit var progressBar: LinearProgressIndicator
    lateinit var preferences: SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)
        progressBar=findViewById(R.id.loadingBar)
        preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setNotificationChannel()
        }


       val transitionXml = TransitionInflater.from(this).inflateTransition(R.transition.descripcion).apply {
            excludeTarget(window.decorView.findViewById<View>(R.id.action_bar_container), true)
            excludeTarget(android.R.id.statusBarBackground, true)
            excludeTarget(android.R.id.navigationBarBackground, true)
        }
        window.exitTransition = transitionXml




        /*Inicializa frament segun su origen
        * Se es por Inicio seccion o registrar o DETAIL*/
        val origen : String? = intent.getStringExtra("origen")

        when (origen) {
            "DETAIL" -> {
                var prodcutoDesdeCarrito = intent.getParcelableExtra<ProductoApi>(Intent.EXTRA_INDEX)
                val bundleCarrito : Bundle = Bundle()
                bundleCarrito.putParcelable(SHOP_PRODUCT, prodcutoDesdeCarrito)
                replaceFragment(carritoFragment,
                    bundleCarrito)
                bottom_navigation.selectedItemId = R.id.menu_carrito

                Toast.makeText(this, "Producto Agregado", Toast.LENGTH_SHORT).show()
                true
            }
            "COMPRA" -> {
                replaceFragment(compraFragment, null)
                true
            }
            else -> {
                var bundle : Bundle = Bundle()
                bundle.putString(SHOP_LIST, MainApp.array)
                replaceFragment(listaFragment, bundle)
                bottom_navigation.selectedItemId = R.id.ic_inicio
                false

            }

        }





        /*Controlador del navegador
        * Dependiendo lo que seleccione el usuario en el Bottom Nav te envia a dicho fragment*/
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener {
                item ->
            when (item.itemId) {
                R.id.menu_carrito -> {
                    loading(progressBar)
                    replaceFragment(carritoFragment, null)
                    true
                }

                R.id.menu_usuario -> {
                    loading(progressBar)
                    var bundle = intent.extras //Recuperamos datos de bundle
                    val email = bundle?.getString(USER_EMAIL)
                    val fullName = bundle?.getString(USER_FULL_NAME)
                    val avatar = bundle?.getString(USER_AVATAR)
                    if(email != null) {
                        preferences.edit()
                            .putString(NAME, fullName)
                            .putString(MAIL, email)
                            .putString(AVATAR, avatar)
                            .apply()
                    }

                    val emailBundle = preferences.getString(MAIL,"default")
                    val nameBundle = preferences.getString(NAME, "default")
                    val avatarBundle = preferences.getString(AVATAR,"default")

                    //las asignamos a nuestra colección y aplicamos
                        var bundleFrag = Bundle()//Reasignamos datos de bundle
                        bundleFrag.putString(USER_EMAIL_SHOP, emailBundle)
                        bundleFrag.putString(USER_FULL_NAME_SHOP, nameBundle)
                        bundleFrag.putString(USER_AVATAR_SHOP, avatarBundle)
                        Log.d("Bundle Shop", bundleFrag.toString())
                        replaceFragment(usuarioFragment, bundleFrag)
                    true
                }
                R.id.ic_inicio -> {
                    loading(progressBar)
                    getProductsList(listaFragment)
                    true
                }
                else -> {
                    false
                }
            }

        }

        /*Iniciar actividad Detail*/

        listaFragment.setListener{
            val detailFragment = supportFragmentManager.findFragmentById(R.id.fragmentDetail) as? DetailFragment

            // Pantalla grande, mostrar detalle en el fragment
            if(detailFragment!=null){
                detailFragment.showProduct(it)
            } else{ //pantalla pequeña, navegar a un nuevo Activity
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra(DetailActivity.PRODUCT,it)

                //!! Transition sin terminar para el detail

                // EN ESTA SECCION PROBE DE USAR SHARED TRANSITION PERO NO ME GUSTO EL RESULTADO, PREGUNTAR A JAVI

                /* val options = ViewCompat.getTransitionName(findViewById(R.id.cardviewLista))?.let {
                     ActivityOptionsCompat.makeSceneTransitionAnimation(
                         this, findViewById(R.id.cardviewLista), it
                     )
                 }*/

                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            }
        }


    }
        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            // Handle item selection
            return when (item.itemId) {
                R.id.search -> {
                    Toast.makeText(this, "Función no disponible", Toast.LENGTH_SHORT).show()
                    true

                }
                R.id.help -> {
                    val url = "https://www.bedu.org"
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    startActivity(i)
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }
        // Funcion que te levanta el fragment que se pasa por parametro
        private fun replaceFragment(fragment: Fragment, bundle:Bundle?){
            fragment.arguments = bundle//Enviamos Bundle, de existir
            val trans = supportFragmentManager.beginTransaction()
            trans.replace(R.id.fragemento_contenedor, fragment)
            trans.addToBackStack(null)
            trans.commit()
        }

        private fun getProductsList(fragment: Fragment, id: Int = 0){
        var products: MutableList<ProductoApi> = mutableListOf()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create<WebServices>(WebServices::class.java)
            service.getAllProducts().enqueue(object : Callback<MutableList<ProductoApi>> {

               override fun onResponse(
                    call: Call<MutableList<ProductoApi>>,
                    response: Response<MutableList<ProductoApi>>
                ) {
                    products = response.body()!!
                    Log.d("json?", products.toString())
                    Log.i("GsonConverter", Gson().toJson(products))
                    val json = Gson().toJson(products)
                    var bundleFrag = Bundle()
                    bundleFrag.putString(SHOP_LIST, json)
                    replaceFragment(fragment, bundleFrag)
                }

                override fun onFailure(call: Call<MutableList<ProductoApi>>, t: Throwable) {
                    t.printStackTrace()
                }

            })

        /*else if(fragment == carritoFragment){
            service.getProduct(id).enqueue(object: Callback<ProductoApi> {

                override fun onResponse(call: Call<ProductoApi>, response: Response<ProductoApi>) {
                    product = response.body()!!
                    Log.d("json?Id", products.toString())
                    Log.i("GsonConverterId", Gson().toJson(products))
                    val json = Gson().toJson(products)
                    var bundleFrag = Bundle()
                    bundleFrag.putString(SHOP_LIST, json)
                    replaceFragment(fragment, bundleFrag)
                }

                override fun onFailure(call: Call<ProductoApi>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }*/
    }

        override fun onCreateOptionsMenu(menu: Menu): Boolean {
            val inflater: MenuInflater = menuInflater
            inflater.inflate(R.menu.menu_superior, menu)
            return true
        }

    //funcion para activar la barrita de loading en los diferentes fragments
        private fun loading(progressBar: LinearProgressIndicator){
            progressBar.isVisible=true
            AnimatorInflater.loadAnimator(this, R.animator.loading_bar).apply {
                setTarget(progressBar)
                start()
            }
        }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setNotificationChannel(){
        val name = getString(R.string.AgregarCarrito)
        val descriptionText = getString(R.string.app_name)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_OTHERS, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }


    fun compraNotification(){


        val intent = Intent(this, Shop::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("origen", "COMPRA")
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)



        var builder = NotificationCompat.Builder(this, CHANNEL_OTHERS)
            .setSmallIcon(R.drawable.bedu) //seteamos el ícono de la push notification
            .setContentTitle(getString(R.string.title)) //seteamos el título de la notificación
            .setContentText(getString(R.string.body)) //seteamos el cuerpo de la notificación
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) //Ponemos una prioridad por defecto
            .setContentIntent(pendingIntent) //se define aquí el content intend
            .setAutoCancel(true)

        //lanzamos la notificación
        with(NotificationManagerCompat.from(this)) {
            notify(20, builder.build()) //en este caso pusimos un id genérico
        }
    }



    //Funciones para mostrar y ocultar la bottomNavBar

    fun hideBottomNav() {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.visibility = View.GONE
    }

    fun showBottomNav() {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.visibility = View.VISIBLE
    }

    }
