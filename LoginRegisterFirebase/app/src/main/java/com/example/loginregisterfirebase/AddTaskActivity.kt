package com.example.loginregisterfirebase

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddTaskActivity : AppCompatActivity() {

    private lateinit var taskDescriptionEditText: EditText
    private lateinit var addTaskButton: Button
    private lateinit var selectedUserTextView: TextView
    private lateinit var databaseReference: DatabaseReference
    private var selectedUserEmail: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        // Firebase referansı
        databaseReference = FirebaseDatabase.getInstance().reference

        // Görev ekleme ekranındaki view'lar
        selectedUserTextView = findViewById(R.id.selectedUserTextView)
        taskDescriptionEditText = findViewById(R.id.taskDescriptionEditText)
        addTaskButton = findViewById(R.id.addTaskButton)

        // Intent'ten gelen verileri al
        selectedUserEmail = intent.getStringExtra("userEmail")
        selectedUserTextView.text = "Seçilen Kullanıcı: $selectedUserEmail"

        // "Görev Ekle" butonuna tıklanıldığında yapılacak işlem
        addTaskButton.setOnClickListener {
            val taskDescription = taskDescriptionEditText.text.toString().trim()
            if (taskDescription.isNotEmpty()) {
                addTaskToUser(taskDescription)
            } else {
                Toast.makeText(this, "Görev tanımını girin", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addTaskToUser(taskDescription: String) {
        selectedUserEmail?.let { email ->
            // Kullanıcının ID'sine göre Firebase'de görev ekle
            databaseReference.child("staffs").orderByChild("email").equalTo(email)
                .get().addOnSuccessListener { dataSnapshot ->
                    if (dataSnapshot.exists()) {
                        for (snapshot in dataSnapshot.children) {
                            val userId = snapshot.key
                            val taskRef = databaseReference.child("tasks").push()
                            val taskId = taskRef.key
                            val taskMap = mapOf(
                                "description" to taskDescription,
                                "assigned_to" to email,
                                "state" to "pending"
                            )
                            taskId?.let {
                                taskRef.setValue(taskMap).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(this, "Görev başarıyla eklendi", Toast.LENGTH_SHORT).show()
                                        finish()
                                    } else {
                                        Toast.makeText(this, "Görev eklenirken hata oluştu", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    } else {
                        Toast.makeText(this, "Kullanıcı bulunamadı", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
