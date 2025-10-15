package com.lilist.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TaskStorage(private val context: Context) {

    private val prefs = context.getSharedPreferences("todo_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveTasks(tasks: List<Task>) {
        val json = gson.toJson(tasks)
        prefs.edit().putString("tasks", json).apply()
    }

    fun loadTasks(): List<Task> {
        val json = prefs.getString("tasks", null)
        return try {
            if (json != null) {
                gson.fromJson(json, object : TypeToken<List<Task>>() {}.type)
            } else emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

}
