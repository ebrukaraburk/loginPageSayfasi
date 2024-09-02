package com.example.loginregisterfirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class KimsinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kimsin)

        val btnYonetici: Button = findViewById(R.id.btnYonetici)
        val btnUye: Button = findViewById(R.id.btnUye)

        // Yönetici butonuna tıklama olayını tanımla
        btnYonetici.setOnClickListener {
            val intent = Intent(this, YoneticiLogin::class.java)
            startActivity(intent)
        }

        // Üye butonuna tıklama olayını tanımla
        btnUye.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }
}
