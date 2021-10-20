package org.bedu.bedushop

import org.bedu.bedushop.ProductoApi
import org.bedu.bedushop.CarritoHolder
import java.util.ArrayList

class CarritoHolder private constructor() {
    val listaCarrito: MutableList<ProductoApi> = mutableListOf()

    companion object {
        var instance: CarritoHolder? = null
            get() {
                if (field == null) {
                    field = CarritoHolder()
                }
                return field
            }
            private set
    }
}