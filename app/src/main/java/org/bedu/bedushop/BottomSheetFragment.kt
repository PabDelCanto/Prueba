package org.bedu.bedushop

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.content.DialogInterface
import android.location.Address
import android.location.Geocoder
import java.util.*


class BottomSheetFragment(): BottomSheetDialogFragment() {
    companion object{
        const val PERMISSION_ID = 33
    }
    private lateinit var direccion : TextView

    lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_fragment,container,false)
        direccion = view.findViewById(R.id.locacionTV)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        //Funcionalidad botones BottomSheet


        val botonCerrar = view.findViewById<Button>(R.id.cerrar).setOnClickListener {
            dismiss()
        }

        //Actualiza al momento de abrir la pantalla
        getLocation()

        //Boton para actualizar la localización
        val botonActualizar = view.findViewById<Button>(R.id.actualizarDir).setOnClickListener {
            getLocation()
        }


    }
    private fun checkGranted(permission: String): Boolean{
        return ActivityCompat.checkSelfPermission(requireActivity(), permission) == PackageManager.PERMISSION_GRANTED
    }

    
    private fun checkPermissions(): Boolean {
        if ( checkGranted(Manifest.permission.ACCESS_COARSE_LOCATION) &&
            checkGranted(Manifest.permission.ACCESS_COARSE_LOCATION) ){
            return true
        }
        return false
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }
    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    /*
    Funcion que trae la localización
    */
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (checkPermissions()) { //verificamos si tenemos permisos
            if (isLocationEnabled()) { //localizamos sólo si el GPS está encendido

                mFusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
                    val geocoder = Geocoder(requireActivity(), Locale.getDefault())
                    val addresses: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)

                    val addressItem = addresses.first()
                    val addressFragments = (0 .. addressItem.maxAddressLineIndex).map { i ->
                        addressItem.getAddressLine(i)
                    }

                    direccion.text = addressFragments.first()

                }
            }
        } else{
            //si no se tiene permiso, pedirlo
            requestPermissions()
        }
    }

}