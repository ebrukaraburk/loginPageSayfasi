package com.example.loginregisterfirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class Login : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference

    private lateinit var tcEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        databaseReference = FirebaseDatabase.getInstance().reference

        tcEditText = findViewById(R.id.tc)
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.loginBtn)
        registerTextView = findViewById(R.id.registerNowBtn)

        loginButton.setOnClickListener {
            val tcTxt = tcEditText.text.toString().trim()
            val emailTxt = emailEditText.text.toString().trim()
            val passwordTxt = passwordEditText.text.toString().trim()

            if (tcTxt.isNotEmpty() && emailTxt.isNotEmpty() && passwordTxt.isNotEmpty()) {
                checkLogin(tcTxt, emailTxt, passwordTxt)
            } else {
                Toast.makeText(this, "Lütfen TC, email ve şifrenizi girin", Toast.LENGTH_SHORT).show()
            }
        }

        registerTextView.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
    }

    private fun checkLogin(tc: String, email: String, password: String) {
        databaseReference.child("users").child(email.replace(".", ",")).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val storedPassword = snapshot.child("password").getValue(String::class.java)
                    val storedTc = snapshot.child("tc").getValue(String::class.java)

                    if (storedPassword != null && storedPassword == password && storedTc == tc) {
                        Toast.makeText(this@Login, "Giriş başarılı!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@Login, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@Login, "Geçersiz TC, email veya şifre", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@Login, "Kullanıcı bulunamadı", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Login, "Veritabanı hatası: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
