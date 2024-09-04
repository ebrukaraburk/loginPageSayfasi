package com.example.loginregisterfirebase

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Register : AppCompatActivity() {
    private lateinit var services: Services

    private lateinit var emailEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        services = Services(this)

        emailEditText = findViewById(R.id.email)
        nameEditText = findViewById(R.id.name)
        surnameEditText = findViewById(R.id.surname)
        passwordEditText = findViewById(R.id.password)
        confirmPasswordEditText = findViewById(R.id.confirmPassword)
        registerButton = findViewById(R.id.registerBtn)

        registerButton.setOnClickListener {
            val emailTxt = emailEditText.text.toString().trim()
            val nameTxt = nameEditText.text.toString().trim()
            val surnameTxt = surnameEditText.text.toString().trim()
            val passwordTxt = passwordEditText.text.toString().trim()
            val confirmPasswordTxt = confirmPasswordEditText.text.toString().trim()

            if (emailTxt.isNotEmpty() && nameTxt.isNotEmpty() && surnameTxt.isNotEmpty() && passwordTxt.isNotEmpty() && confirmPasswordTxt.isNotEmpty()) {
                if (passwordTxt == confirmPasswordTxt) {
                    services.registerUser(emailTxt, nameTxt, surnameTxt, passwordTxt) { success, message ->
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        if (success) finish()
                    }
                } else {
                    Toast.makeText(this, "Şifreler eşleşmiyor", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Lütfen tüm bilgileri girin", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
