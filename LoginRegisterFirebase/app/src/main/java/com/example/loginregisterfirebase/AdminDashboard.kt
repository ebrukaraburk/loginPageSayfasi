package com.example.loginregisterfirebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class AdminDashboard : AppCompatActivity() {

    private lateinit var memberRecyclerView: RecyclerView
    private lateinit var memberAdapter: UserAdapter
    private lateinit var memberList: ArrayList<User>
    private lateinit var databaseReference: DatabaseReference
    private var selectedUser: User? = null // Seçilen kullanıcıyı tutacak değişken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        // Başlık TextView'i
        val userHeaderTextView: TextView = findViewById(R.id.usersHeaderTextView)
        userHeaderTextView.text = "Kullanıcılar"

        // Kullanıcı RecyclerView ve Adapter kurulumu
        memberRecyclerView = findViewById(R.id.memberRecyclerView)
        memberRecyclerView.layoutManager = LinearLayoutManager(this)
        memberList = ArrayList()
        memberAdapter = UserAdapter(memberList) { user ->
            selectUser(user)
        }
        memberRecyclerView.adapter = memberAdapter

        // "Görev Ekle" butonunu tanımla
        val addTaskButton: Button = findViewById(R.id.addTaskButton)
        addTaskButton.setOnClickListener {
            selectedUser?.let { user ->
                // Görev ekleme işlemini başlat
                addTaskToUser(user)
            } ?: Toast.makeText(this, "Bir kullanıcı seçin", Toast.LENGTH_SHORT).show()
        }

        // Firebase referansı
        databaseReference = FirebaseDatabase.getInstance().reference

        // Firebase'den kullanıcı verilerini çek
        getUsersFromDatabase()
    }

    private fun getUsersFromDatabase() {
        databaseReference.child("staffs").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                memberList.clear()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    if (user != null) {
                        memberList.add(user)
                    }
                }
                memberAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("AdminDashboard", "Database error: ${error.message}")
                Toast.makeText(this@AdminDashboard, "Veri çekme hatası: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun selectUser(user: User) {
        selectedUser = user
        Toast.makeText(this, "Seçilen kullanıcı: ${user.name} ${user.surname}", Toast.LENGTH_SHORT).show()
    }

    private fun addTaskToUser(user: User) {
        val intent = Intent(this, AddTaskActivity::class.java)
        intent.putExtra("selectedUser", user)
        startActivity(intent)
    }
}
