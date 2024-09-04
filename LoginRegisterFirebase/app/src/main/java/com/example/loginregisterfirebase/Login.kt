package com.example.loginregisterfirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Login : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerTextView: TextView

    // Services sınıfını tanımla
    private lateinit var services: Services

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Services sınıfını başlat
        services = Services(this)

        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.loginBtn)
        registerTextView = findViewById(R.id.registerNowBtn)

        loginButton.setOnClickListener {
            val emailTxt = emailEditText.text.toString().trim()
            val passwordTxt = passwordEditText.text.toString().trim()

            if (emailTxt.isNotEmpty() && passwordTxt.isNotEmpty()) {
                services.checkStaffLogin(emailTxt, passwordTxt) { isLoginSuccessful ->
                    if (isLoginSuccessful) {
                        Toast.makeText(this, "Giriş başarılı!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Geçersiz email veya şifre", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Lütfen email ve şifrenizi girin", Toast.LENGTH_SHORT).show()
            }
        }

        // Register sayfasına geçiş
        registerTextView.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
}
