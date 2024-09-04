package com.example.loginregisterfirebase

data class User(
    val id: String = "",
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val task: String? = null // Görev bilgisi
)
