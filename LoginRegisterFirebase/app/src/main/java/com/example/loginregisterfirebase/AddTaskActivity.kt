package com.example.loginregisterfirebase

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var taskSpinner: Spinner
    private lateinit var startDateButton: Button
    private lateinit var endDateButton: Button
    private lateinit var confirmTaskButton: Button

    private lateinit var selectedUser: User
    private lateinit var services: Services

    private var startDate: String = ""
    private var endDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        taskSpinner = findViewById(R.id.taskSpinner)
        startDateButton = findViewById(R.id.startDateButton)
        endDateButton = findViewById(R.id.endDateButton)
        confirmTaskButton = findViewById(R.id.confirmTaskButton)

        selectedUser = intent.getSerializableExtra("selectedUser") as User

        services = Services(this)

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
            if (taskSpinner.selectedItem.toString().equals("select a task")) {
                Toast.makeText(this, "Please Select a task", Toast.LENGTH_SHORT).show()
            } else {
                val selectedTask = taskSpinner.selectedItem.toString()
                services.addTaskToUser(selectedTask, selectedUser, startDate, endDate) { success ->
                    if (success) {
                        finish() // Close the activity
                    }
                }
            }
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
}
