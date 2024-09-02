package com.example.loginregisterfirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class YoneticiLogin : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yonetici_login) // Create a separate layout for admin login

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().reference

        // Bind UI elements
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.loginBtn)

        // Set click listener for the login button
        loginButton.setOnClickListener {
            val emailTxt = emailEditText.text.toString()
            val passwordTxt = passwordEditText.text.toString()

            // Check if the email and password fields are not empty
            if (emailTxt.isNotEmpty() && passwordTxt.isNotEmpty()) {
                checkAdminLogin(emailTxt, passwordTxt)
            } else {
                Toast.makeText(this, "Lütfen email ve şifrenizi girin", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkAdminLogin(email: String, password: String) {
        // Access the "admins" node in Firebase to check for admin credentials
        databaseReference.child("admins").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var isLoginSuccessful = false

                // Iterate through each child under "admins"
                for (adminSnapshot in snapshot.children) {
                    val storedEmail = adminSnapshot.child("email").getValue(String::class.java)
                    val storedPassword = adminSnapshot.child("password").getValue(String::class.java)

                    // Check if the stored credentials match the entered credentials
                    if (storedEmail != null && storedPassword != null) {
                        if (storedEmail == email && storedPassword == password) {
                            isLoginSuccessful = true
                            break
                        }
                    }
                }

                // If login is successful, navigate to AdminDashboard
                if (isLoginSuccessful) {
                    Toast.makeText(this@YoneticiLogin, "Giriş başarılı!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@YoneticiLogin, AdminDashboard::class.java))
                    finish()
                } else {
                    // Show an error message if login fails
                    Toast.makeText(this@YoneticiLogin, "Geçersiz email veya şifre", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
                Toast.makeText(this@YoneticiLogin, "Veritabanı hatası: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
