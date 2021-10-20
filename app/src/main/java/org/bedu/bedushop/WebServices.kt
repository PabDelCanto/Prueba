package org.bedu.bedushop


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface WebServices {
    // Definimos el endpoint pokemont/{pokemon}, siendo este último una variable a ingresar por el usuario (en este caso, desde el edit text)
    @GET("products/{id}/")
    fun getProduct(@Path("id") id: Int): Call<ProductoApi> //la clase Pokemon dentro de Call indica que el json de respuesta va a   ser transformado es un objeto de esa clase.

    @GET("products/")
    fun getAllProducts() : Call<MutableList<ProductoApi>>

    @POST("products/{id}/")
    fun editProductById(@Path("id") id:Int, @Body product: ProductoApi?):Call<ProductoApi>
}