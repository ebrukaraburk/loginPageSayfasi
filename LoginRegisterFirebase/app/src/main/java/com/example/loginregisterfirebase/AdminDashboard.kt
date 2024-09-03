package com.example.loginregisterfirebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        // Kullanıcı RecyclerView ve Adapter kurulumu
        memberRecyclerView = findViewById(R.id.memberRecyclerView)
        memberRecyclerView.layoutManager = LinearLayoutManager(this)
        memberList = ArrayList()
        memberAdapter = UserAdapter(memberList) { user ->
            viewUserDetails(user)
        }
        memberRecyclerView.adapter = memberAdapter

        // Firebase referansı
        databaseReference = FirebaseDatabase.getInstance().reference

        // Firebase'den kullanıcı verilerini çek
        getUsersFromDatabase()
    }

    private fun getUsersFromDatabase() {
        // "staffs" grubundaki kullanıcıları çek
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

    private fun viewUserDetails(user: User) {
        // Kullanıcı detaylarını göstermek için gerekli işlemler
        Toast.makeText(this, "İncele: ${user.name} ${user.surname}", Toast.LENGTH_SHORT).show()

        // Örneğin, yeni bir aktivite başlatabilirsiniz
        // val intent = Intent(this, UserDetailActivity::class.java)
        // intent.putExtra("user", user)
        // startActivity(intent)
    }
}
