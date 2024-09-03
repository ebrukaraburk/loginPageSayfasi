package com.example.loginregisterfirebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private val taskList: List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    // ViewHolder sınıfı
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // UI bileşenlerini buraya tanımlayın
        val titleTextView: TextView = itemView.findViewById(R.id.taskTitleTextView)
        val assignedToTextView: TextView = itemView.findViewById(R.id.taskAssignedToTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        // Veriyi UI bileşenlerine bağlayın
        holder.titleTextView.text = task.title
        holder.assignedToTextView.text = task.assignedTo
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}
