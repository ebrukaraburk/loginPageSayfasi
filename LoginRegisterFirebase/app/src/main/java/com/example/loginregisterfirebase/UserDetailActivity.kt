package com.example.loginregisterfirebase

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserDetailActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var taskEditText: EditText
    private lateinit var assignTaskButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        val userName = intent.getStringExtra("userName")
        val userSurname = intent.getStringExtra("userSurname")
        val userId = intent.getStringExtra("userId")

        val nameTextView: TextView = findViewById(R.id.userName)
        val surnameTextView: TextView = findViewById(R.id.userSurname)
        taskEditText = findViewById(R.id.taskEditText)
        assignTaskButton = findViewById(R.id.assignTaskButton)

        nameTextView.text = userName
        surnameTextView.text = userSurname

        databaseReference = FirebaseDatabase.getInstance().reference

        assignTaskButton.setOnClickListener {
            val task = taskEditText.text.toString().trim()
            if (task.isNotEmpty() && userId != null) {
                assignTaskToUser(userId, task)
            } else {
                Toast.makeText(this, "Görev girin", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun assignTaskToUser(userId: String, task: String) {
        val taskId = databaseReference.child("users").child(userId).child("tasks").push().key
        if (taskId != null) {
            val taskMap = mapOf("task" to task)
            databaseReference.child("users").child(userId).child("tasks").child(taskId)
                .setValue(taskMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Görev başarıyla atandı", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Görev atama hatası: ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }
}
