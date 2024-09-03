package com.example.loginregisterfirebase

import com.google.firebase.database.*
import android.content.Context
import android.util.Log
import android.widget.Toast

class Services(private val context: Context) {
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun checkAdminLogin(email: String, password: String, onLoginResult: (Boolean) -> Unit) {
        databaseReference.child("admins").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var isLoginSuccessful = false

                for (adminSnapshot in snapshot.children) {
                    val storedEmail = adminSnapshot.child("email").getValue(String::class.java)
                    val storedPassword = adminSnapshot.child("password").getValue(String::class.java)

                    Log.d("Services", "Checking ID: ${adminSnapshot.key}, Email: $storedEmail, Password: $storedPassword")

                    if (storedEmail != null && storedPassword != null) {
                        if (storedEmail == email && storedPassword == password) {
                            isLoginSuccessful = true
                            break
                        }
                    }
                }

                onLoginResult(isLoginSuccessful)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Veritabanı hatası: ${error.message}", Toast.LENGTH_SHORT).show()
                onLoginResult(false)
            }
        })
    }



    fun checkStaffLogin(email: String, password: String, onLoginResult: (Boolean) -> Unit) {
        databaseReference.child("staffs").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var isLoginSuccessful = false

                for (adminSnapshot in snapshot.children) {
                    val storedEmail = adminSnapshot.child("email").getValue(String::class.java)
                    val storedPassword = adminSnapshot.child("password").getValue(String::class.java)

                    Log.d("Services", "Checking ID: ${adminSnapshot.key}, Email: $storedEmail, Password: $storedPassword")

                    if (storedEmail != null && storedPassword != null) {
                        if (storedEmail == email && storedPassword == password) {
                            isLoginSuccessful = true
                            break
                        }
                    }
                }

                onLoginResult(isLoginSuccessful)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Veritabanı hatası: ${error.message}", Toast.LENGTH_SHORT).show()
                onLoginResult(false)
            }
        })
    }




}
