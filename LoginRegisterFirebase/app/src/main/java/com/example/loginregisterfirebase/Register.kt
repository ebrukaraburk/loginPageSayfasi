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
    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        databaseReference = FirebaseDatabase.getInstance().reference

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
                    registerUser(emailTxt, nameTxt, surnameTxt, passwordTxt)
                } else {
                    Toast.makeText(this, "Şifreler eşleşmiyor", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Lütfen tüm bilgileri girin", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(email: String, name: String, surname: String, password: String) {
        val staff = mapOf(
            "email" to email,
            "name" to name,
            "surname" to surname,
            "password" to password
        )
        
        databaseReference.child("staff").get().addOnSuccessListener { snapshot ->
            val id = (snapshot.childrenCount + 1).toString()

            databaseReference.child("staff").child(id).setValue(staff)
                .addOnSuccessListener {
                    Toast.makeText(this, "Kayıt başarılı!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Kayıt hatası: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }.addOnFailureListener {
            Toast.makeText(this, "Veritabanı hatası: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

}
