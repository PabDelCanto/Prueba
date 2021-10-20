package org.bedu.bedushop

import RecyclerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_recycler.*
import kotlinx.android.synthetic.main.fragment_usuario.*
import okhttp3.*
import org.json.JSONObject
import org.w3c.dom.Text
import java.io.IOException
import java.util.jar.Manifest
import kotlin.random.Random


class UsuarioFragment : Fragment() {

    val PREFS_NAME = "org.bedu.bedushop"
    val NAME = "NAME"
    val MAIL = "MAIL"
    val AVATAR = "AVATAR"

    lateinit var preferences: SharedPreferences

    private lateinit var txtUser : TextView
    private lateinit var txtEmail : TextView
    private lateinit var imgUser : ImageView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
       val view = inflater.inflate(R.layout.fragment_usuario, container, false)
        val opciones = listOf(
        Opciones("Mis direcciones", R.drawable.ic_location,R.drawable.arrow),
        Opciones("Metodo de pago", R.drawable.credit_card,R.drawable.arrow),
        Opciones("Pedidos", R.drawable.history,R.drawable.arrow),
        Opciones("Notificaciones", R.drawable.notificacion,R.drawable.arrow),
        Opciones("Cambiar contraseña", R.drawable.lock,R.drawable.arrow)
    )

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerOptions)
        recycler.adapter = RecyclerAdapterOpciones(opciones){
           //listener del recyclerview para desplegar la opcion seleccionada
            val direccion = this.arguments?.getString("DIRECCION")
            val bts : BottomSheetFragment = BottomSheetFragment()
            if(it.opcion.toString() == "Mis direcciones"){
            bts.show(parentFragmentManager,"fragment")

            }
        }

        txtUser = view.findViewById(R.id.namePerfil)
        txtEmail = view.findViewById(R.id.emailPerfil)
        imgUser = view.findViewById(R.id.imgPerfil)

        //Recuperamos datos del bundle desde la actividad shop
        val userMail = this.arguments?.getString(USER_EMAIL_SHOP)
        val userFullName = this.arguments?.getString(USER_FULL_NAME_SHOP)
        val userAvatar = this.arguments?.getString(USER_AVATAR_SHOP)


        //Asignamos los datos
        txtUser.text = userFullName
        txtEmail.text = userMail
        Picasso.with(context).load(userAvatar).into(imgUser)


        return view
    }

}

