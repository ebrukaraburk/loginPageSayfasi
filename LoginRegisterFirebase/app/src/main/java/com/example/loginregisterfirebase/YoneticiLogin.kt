package com.example.loginregisterfirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class YoneticiLogin : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    private lateinit var services: Services

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yonetici_login)

        services = Services(this) // Services sınıfını başlatın

        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.loginBtn)

        loginButton.setOnClickListener {
            val emailTxt = emailEditText.text.toString()
            val passwordTxt = passwordEditText.text.toString()

            if (emailTxt.isNotEmpty() && passwordTxt.isNotEmpty()) {
                services.checkAdminLogin(emailTxt, passwordTxt) { isLoginSuccessful ->
                    if (isLoginSuccessful) {
                        Toast.makeText(this, "Giriş başarılı!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, AdminDashboard::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Geçersiz email veya şifre", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Lütfen email ve şifrenizi girin", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
