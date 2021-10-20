package org.bedu.bedushop

import android.app.ActivityOptions
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import androidx.core.app.ActivityCompat.startActivityForResult
import android.content.Intent
import android.text.TextUtils
import android.transition.Slide
import android.transition.TransitionInflater
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.material.textfield.TextInputEditText

class Registrar : AppCompatActivity() {

    private lateinit var name: TextInputEditText
    private lateinit var mail: TextInputEditText
    private lateinit var phone: TextInputEditText
    private lateinit var pass: TextInputEditText
    private lateinit var registro: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)



        name= findViewById(R.id.completeNameR)
        mail= findViewById(R.id.editTextEmailR)
        phone=findViewById(R.id.phoneR)
        pass = findViewById(R.id.editPasswordR)

        registro = findViewById(R.id.btnRegistrar)

        // Funcion que valida los datos del formulario de registro
        fun validarForm(): Boolean {
            var esValido = true

            fun validarDato(var1:TextInputEditText) {
                if (TextUtils.isEmpty(var1.getText())) {
                var1.error = getString(R.string.requerido)
                esValido = false
            } else var1.error = null
            }

            validarDato(name)
            validarDato(mail)
            validarDato(phone)
            validarDato(pass)
            return esValido
        }

        // Boton de registrar, valida el formulario y te envia el Inicio de Seccion
        registro.setOnClickListener{

            if(validarForm()){
                val intent=Intent(this, MainActivity::class.java).apply {  }
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
            }
            }


        }
    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
    }
}


