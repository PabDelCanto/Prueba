package org.bedu.bedushop

data class User (
    val id: Int ?= 0,
    val email: String ?= "email",
    val first_name: String ?= "Usuario",
    val last_name: String ?= "",
    val avatar: String ?= "@drawable/person_black"
    ){}