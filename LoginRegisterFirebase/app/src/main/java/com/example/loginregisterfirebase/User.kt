package com.example.loginregisterfirebase

import java.io.Serializable

data class User(
    val id: String = "",
    val name: String = "",
    val surname: String = "",
    val email: String = ""
) : Serializable
