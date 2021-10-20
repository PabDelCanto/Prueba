package org.bedu.bedushop

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.telecom.Call
import android.text.TextUtils
import android.transition.TransitionInflater
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.isEmpty
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import io.realm.Realm
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import okhttp3.FormBody
import org.json.JSONObject
import kotlin.random.Random

//Variables globales para bundle
const val USER_EMAIL = "org.bedu.bedushop.USER_EMAIL"
const val USER_FULL_NAME = "org.bedu.bedushop.USER_FULL_NAME"
const val USER_AVATAR = "org.bedu.bedushop.USER_AVATAR"

class MainActivity : AppCompatActivity() {

    private val baseUrl = "https://reqres.in/api/users/" //URL API
    private lateinit var mail:TextInputEditText
    private lateinit var pass: TextInputEditText
    private lateinit var registro: Button
    private lateinit var inicio: Button
    private lateinit var layout: LinearLayout

    private lateinit var products: List<Product>
    //Flag para inicio sesión

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Llamo al Real para construir los productos cuando se inicia la app aunque no los use en esta pantalla


        //TODO: Checkear como resetear logInFlag cuando se regrese a menú de logIn

        //! TRANSITIONS A MEJORAR(SOLVED)
        /* val transitionXml = TransitionInflater.from(this).inflateTransition(R.transition.login).apply {
            excludeTarget(window.decorView.findViewById<View>(R.id.action_bar_container), true)
            excludeTarget(android.R.id.statusBarBackground, true)
            excludeTarget(android.R.id.navigationBarBackground, true)
        }
        window.exitTransition = transitionXml
        //! TRANSITIONS A MEJORAR*/

        //cargo los datos del login
        mail = findViewById(R.id.editTextEmail)
        pass = findViewById(R.id.editPassword)
        registro = findViewById(R.id.registro)
        inicio = findViewById(R.id.inicio)
        layout = findViewById(R.id.loginLayout)

        //Llamando datos de API Login y utilizandolos mediante booleano
        fun checkMail(email: String, pass: String): Boolean {

            //instanciando al cliente
            val okHttpClient = OkHttpClient()
            val url = "https://reqres.in/api/login/"
            var flag = false

            //Instancio cuerpo del request para hacer POST
            val formBody = FormBody.Builder()
                .add("email", email)
                .add("password", pass)
                .build();

            //El objeto Request contiene todos los parámetros de la petición (headers, url, method, body etc)
            val request = Request.Builder()
                .url(url)
                .post(formBody)
                .build()

            //enviando y recibiendo las llamadas de forma sincrónica (Porque LogIn no debe iniciar sin previa respuesta)

            try {
                val response = okHttpClient.newCall(request).execute()
                val body = response.body?.string()


                val bodyObj = JSONObject(body)
                if (!bodyObj.has("error")) {
                    flag = true
                }
                else{
                    flag = false
                    runOnUiThread {
                        showSnackBar()

                        //Toast.makeText( this, "Mail o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    }

                    return flag
                }
            }
            catch (e: Error) {
                Log.d("Error", e.toString())
                flag = false
            }

            return flag
        }




        //valido que los datos del login no encuentren vacios, limitando el tipo de entrado
        fun validarForm(): Boolean {
            var esValido = true

            if (TextUtils.isEmpty(mail.getText())) {
                loginSnackBar()
                //mail.error = getString(R.string.error1)
                esValido = false
            } else mail.error = null

            if (TextUtils.isEmpty(pass.getText())) {
                loginSnackBar()
                //pass.error = getString(R.string.error1)
                esValido = false
            } else pass.error = null

            return esValido
        }

        // Si se selecciona el boton de inciar seccion, se valida los datos y si estan correcto se redireccionan al Activity Shop
        inicio.setOnClickListener{

            if(validarForm()){
                //Inicio un nuevo Thread donde tendrá lugar la verificación con la API
                Thread{
                if(checkMail(mail.text.toString(), pass.text.toString())){
                    var user = getUserData()
                    Log.d("user getUserData", user.toString())
                    val bundle = Bundle()//Creamos bundle con datos de usuario
                    bundle.putString(USER_EMAIL, user?.email)
                    bundle.putString(USER_FULL_NAME, "${user?.first_name} ${user?.last_name}")
                    bundle.putString(USER_AVATAR, user?.avatar)
                    val intent=Intent(this, Shop::class.java).apply {
                        putExtras(bundle)//Enviamos bundle a activity shop
                        Log.d("Bundle Login", bundle.toString())
                    }
                    startActivity(intent)
                }
                }.start()
            }
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
        }

        // Si se selecciona el registro te envia al Activity de Registrar
        registro.setOnClickListener{
            val intent=Intent(this, Registrar::class.java).apply {  }
            startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
        }

    }

    //Obtenemos datos del usuario que corresponderán al perfil
    fun getUserData():User?{

        var user : User ?= null
        //instanciando al cliente
        val okHttpClient = OkHttpClient()

        //obteniendo la url completa
        val userId = Random.nextInt(1,12) //son 60 planetas
        val url = "$baseUrl$userId"
        Log.d("url: ", url)

        val request =  Request.Builder()
            .url(url)
            .build()

        try {
            val response = okHttpClient.newCall(request).execute()
            val body = response.body?.string()
            val json = JSONObject(body)
            val jsonStr = json.getString("data")
            Log.d("data?: ", jsonStr.toString())
            val jsonUser = JSONObject(jsonStr)
            Log.d("data JSON?: ", jsonUser.toString())
            val email = jsonUser.getString("email")
            val name = jsonUser.getString("first_name")
            val lastName = jsonUser.getString("last_name")
            val avatar = jsonUser.getString("avatar")
            user = User(userId, email, name, lastName, avatar)
            Log.d("body: ", body.toString())
            Log.d("user: ", user.toString())

        } catch (e: Error){
            Log.e("Error",e.toString())
        }
        return user
    }

    //FUNCIONES PARA SNACKBARS

    //snackbar en caso que el usuario sea incorrecto

    fun showSnackBar(){
        val snackbar: Snackbar = Snackbar.make(layout,"Usuario y/o contraseña invalido", Snackbar.LENGTH_SHORT)
        snackbar.show()

    }

    //snackbar en caso que algun campo no se haya completado

    fun loginSnackBar(){
        val snackbar: Snackbar = Snackbar.make(layout,"Te falta llenar algun campo", Snackbar.LENGTH_INDEFINITE)
            .setAction("ENTENDIDO", View.OnClickListener {
                val snackBarUndo: Snackbar = Snackbar.make(layout, "Gracias",Snackbar.LENGTH_SHORT)
               // snackBarUndo.show()
            })
        snackbar.show()
    }


    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
    }



}
