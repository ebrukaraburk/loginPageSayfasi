package com.example.loginregisterfirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class AdminDashboard : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private lateinit var userList: ArrayList<User>
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        // RecyclerView ve adapter kurulum
        userRecyclerView = findViewById(R.id.userRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userList = ArrayList()
        userAdapter = UserAdapter(userList) { user ->
            viewUserDetails(user)
        }
        userRecyclerView.adapter = userAdapter

        // Firebase referansı
        databaseReference = FirebaseDatabase.getInstance().reference

        // Kullanıcıları Firebase'den çek
        getUsersFromDatabase()
    }

    private fun getUsersFromDatabase() {
        databaseReference.child("staff").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    if (user != null) {
                        userList.add(user)
                    }
                }
                userAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AdminDashboard, "Veri çekme hatası: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun viewUserDetails(user: User) {
        // Kullanıcı detaylarını göstermek için UserDetailActivity'e yönlendir
        val intent = Intent(this, UserDetailActivity::class.java)
        intent.putExtra("userName", user.name)
        intent.putExtra("userSurname", user.surname)
        startActivity(intent)
    }
}
