package com.example.loginregisterfirebase

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var taskSpinner: Spinner
    private lateinit var startDateButton: Button
    private lateinit var endDateButton: Button
    private lateinit var confirmTaskButton: Button

    private lateinit var selectedUser: User
    private lateinit var databaseReference: DatabaseReference

    private var startDate: String = ""
    private var endDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        // Initialize views
        taskSpinner = findViewById(R.id.taskSpinner)
        startDateButton = findViewById(R.id.startDateButton)
        endDateButton = findViewById(R.id.endDateButton)
        confirmTaskButton = findViewById(R.id.confirmTaskButton)

        // Get the selected user
        selectedUser = intent.getSerializableExtra("selectedUser") as User

        // Firebase reference
        databaseReference = FirebaseDatabase.getInstance().reference

        // Set listeners
        startDateButton.setOnClickListener {
            pickDate { date ->
                startDate = date
                startDateButton.text = "Start Date: $date"
            }
        }

        endDateButton.setOnClickListener {
            pickDate { date ->
                endDate = date
                endDateButton.text = "End Date: $date"
            }
        }

        confirmTaskButton.setOnClickListener {
            addTaskToUser()
        }
    }

    private fun pickDate(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(Date(selectedYear - 1900, selectedMonth, selectedDay))
            onDateSelected(date)
        }, year, month, day)
        datePickerDialog.show()
    }

    private fun addTaskToUser() {
        val selectedTask = taskSpinner.selectedItem.toString()

        if (startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, "Please select both start and end dates", Toast.LENGTH_SHORT).show()
            return
        }

        val taskData = mapOf(
            "taskName" to selectedTask,
            "startDate" to startDate,
            "endDate" to endDate
        )

        databaseReference.child("tasks").child(selectedUser.id).push()
            .setValue(taskData).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Task assigned successfully", Toast.LENGTH_SHORT).show()
                    finish() // Close the activity
                } else {
                    Toast.makeText(this, "Failed to assign task", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
