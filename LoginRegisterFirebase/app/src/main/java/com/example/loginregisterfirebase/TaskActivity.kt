package com.example.loginregisterfirebase

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.database.FirebaseDatabase


class TaskActivity : AppCompatActivity() {

    private lateinit var taskTitleEditText: EditText
    private lateinit var assignToEditText: EditText
    private lateinit var addTaskButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        taskTitleEditText = findViewById(R.id.taskTitle)
        assignToEditText = findViewById(R.id.assignTo)
        addTaskButton = findViewById(R.id.addTaskButton)

        addTaskButton.setOnClickListener {
            val title = taskTitleEditText.text.toString().trim()
            val assignedTo = assignToEditText.text.toString().trim()

            if (title.isNotEmpty() && assignedTo.isNotEmpty()) {
                addTaskToFirebase(title, assignedTo)
            } else {
                Toast.makeText(this, "Görev başlığı ve atama bilgilerini girin", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addTaskToFirebase(title: String, assignedToUserId: String) {
        val task = Task(title, assignedToUserId)
        val taskRef = FirebaseDatabase.getInstance().reference.child("tasks").push()
        taskRef.setValue(task)
            .addOnSuccessListener {
                Toast.makeText(this, "Görev başarıyla eklendi!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Görev ekleme hatası: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }


}
