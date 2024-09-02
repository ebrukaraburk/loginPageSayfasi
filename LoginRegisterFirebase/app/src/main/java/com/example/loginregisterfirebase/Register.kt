package com.example.loginregisterfirebase

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference

    private lateinit var tcEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var fullNameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        databaseReference = FirebaseDatabase.getInstance().reference

        tcEditText = findViewById(R.id.tc)
        emailEditText = findViewById(R.id.email)
        fullNameEditText = findViewById(R.id.fullName)
        passwordEditText = findViewById(R.id.password)
        confirmPasswordEditText = findViewById(R.id.confirmPassword)
        registerButton = findViewById(R.id.registerBtn)

        registerButton.setOnClickListener {
            val tcTxt = tcEditText.text.toString().trim()
            val emailTxt = emailEditText.text.toString().trim()
            val fullNameTxt = fullNameEditText.text.toString().trim()
            val passwordTxt = passwordEditText.text.toString().trim()
            val confirmPasswordTxt = confirmPasswordEditText.text.toString().trim()

            if (tcTxt.isNotEmpty() && emailTxt.isNotEmpty() && fullNameTxt.isNotEmpty() && passwordTxt.isNotEmpty() && confirmPasswordTxt.isNotEmpty()) {
                if (passwordTxt == confirmPasswordTxt) {
                    registerUser(tcTxt, emailTxt, fullNameTxt, passwordTxt)
                } else {
                    Toast.makeText(this, "Şifreler eşleşmiyor", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Lütfen tüm bilgileri girin", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(tc: String, email: String, fullName: String, password: String) {
        val user = mapOf(
            "email" to email,
            "fullName" to fullName,
            "password" to password,
            "tc" to tc
        )

        databaseReference.child("users").child(email.replace(".", ",")).setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Kayıt başarılı!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Kayıt hatası: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
