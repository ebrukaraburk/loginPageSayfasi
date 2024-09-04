package com.example.loginregisterfirebase

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class Services(private val context: Context) {
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun registerUser(email: String, name: String, surname: String, password: String, onResult: (Boolean, String) -> Unit) {
        databaseReference.child("staffs").get().addOnSuccessListener { snapshot ->
            val id = (snapshot.childrenCount + 1).toString()

            val userMap = mapOf(
                "email" to email,
                "name" to name,
                "surname" to surname,
                "password" to password
            )

            databaseReference.child("staffs").child(id).setValue(userMap)
                .addOnSuccessListener {
                    onResult(true, "Kayıt başarılı!")
                }
                .addOnFailureListener {
                    onResult(false, "Kayıt hatası: ${it.message}")
                }
        }.addOnFailureListener {
            onResult(false, "Veritabanı hatası: ${it.message}")
        }
    }

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

                for (staffSnapshot in snapshot.children) {
                    val storedEmail = staffSnapshot.child("email").getValue(String::class.java)
                    val storedPassword = staffSnapshot.child("password").getValue(String::class.java)

                    Log.d("Services", "Checking ID: ${staffSnapshot.key}, Email: $storedEmail, Password: $storedPassword")

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

    fun addTaskToUser(selectedTask: String, selectedUser: User, startDate: String, endDate: String, onResult: (Boolean) -> Unit) {
        if (startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(context, "Please select both start and end dates", Toast.LENGTH_SHORT).show()
            onResult(false)
            return
        }

        val taskData = mapOf(
            "startDate" to startDate,
            "endDate" to endDate
        )

        databaseReference.child("tasks").child(selectedTask).setValue(taskData).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                databaseReference.child("staffs").orderByChild("email").equalTo(selectedUser.email)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (staffSnapshot in dataSnapshot.children) {
                                    val staffId = staffSnapshot.key.toString()

                                    databaseReference.child("tasks").child(selectedTask).child("assigned to")
                                        .addListenerForSingleValueEvent(object : ValueEventListener {
                                            override fun onDataChange(assignedToSnapshot: DataSnapshot) {
                                                val assignedToMap = mutableMapOf<String, Any>()

                                                for (userSnapshot in assignedToSnapshot.children) {
                                                    val userId = userSnapshot.key
                                                    val userData = userSnapshot.getValue(Map::class.java) ?: continue
                                                    assignedToMap[userId!!] = userData
                                                }

                                                val newAssignedToData = mapOf(
                                                    "name" to selectedUser.name,
                                                    "surname" to selectedUser.surname,
                                                    "email" to selectedUser.email
                                                )

                                                assignedToMap[staffId] = newAssignedToData

                                                databaseReference.child("tasks").child(selectedTask)
                                                    .child("assigned to").setValue(assignedToMap)
                                                    .addOnCompleteListener { updateTask ->
                                                        if (updateTask.isSuccessful) {
                                                            Toast.makeText(context, "Task assigned successfully", Toast.LENGTH_SHORT).show()
                                                            onResult(true)
                                                        } else {
                                                            Toast.makeText(context, "Failed to assign task", Toast.LENGTH_SHORT).show()
                                                            onResult(false)
                                                        }
                                                    }
                                            }

                                            override fun onCancelled(databaseError: DatabaseError) {
                                                Toast.makeText(context, "Failed to fetch assigned to data", Toast.LENGTH_SHORT).show()
                                                onResult(false)
                                            }
                                        })
                                }
                            } else {
                                Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show()
                                onResult(false)
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Toast.makeText(context, "Failed to fetch user ID", Toast.LENGTH_SHORT).show()
                            onResult(false)
                        }
                    })
            } else {
                Toast.makeText(context, "Failed to add task", Toast.LENGTH_SHORT).show()
                onResult(false)
            }
        }
    }
}
