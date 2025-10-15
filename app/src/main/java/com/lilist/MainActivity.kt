package com.lilist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val taskStorage = TaskStorage(this)
        setContent {
            MainScreen(taskStorage)
        }
    }
}

@Composable
fun MainScreen(taskStorage: TaskStorage) {
    var selectedScreen by remember { mutableStateOf("todo") }
    var tasks by remember { mutableStateOf(taskStorage.loadTasks()) }

    fun agregarTarea(tarea: Task) {
        tasks = tasks + tarea
        taskStorage.saveTasks(tasks)
    }

    fun actualizarTarea(tarea: Task) {
        tasks = tasks.map { if (it.id == tarea.id) tarea else it }
        taskStorage.saveTasks(tasks)
    }

    fun eliminarTarea(tarea: Task) {
        tasks = tasks.filter { it.id != tarea.id }
        taskStorage.saveTasks(tasks)
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.List, contentDescription = "ToDo") },
                    label = { Text("ToDo") },
                    selected = selectedScreen == "todo",
                    onClick = { selectedScreen = "todo" }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.DateRange, contentDescription = "Calendario") },
                    label = { Text("Calendario") },
                    selected = selectedScreen == "calendar",
                    onClick = { selectedScreen = "calendar" }
                )
            }
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            when (selectedScreen) {
                "todo" -> ToDoScreen(
                    tasks = tasks,
                    onAddTask = ::agregarTarea,
                    onUpdateTask = ::actualizarTarea,
                    onDeleteTask = ::eliminarTarea
                )
                "calendar" -> CalendarScreenWrapper(
                    tasks = tasks,
                    onAddTask = ::agregarTarea,
                    onUpdateTask = ::actualizarTarea,
                    onDeleteTask = ::eliminarTarea
                )
            }
        }
    }
}

@Composable
fun CalendarScreenWrapper(
    tasks: List<Task>,
    onAddTask: (Task) -> Unit,
    onUpdateTask: (Task) -> Unit,
    onDeleteTask: (Task) -> Unit
) {
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    CalendarScreen(
        selectedDate = selectedDate,
        onDateSelected = { selectedDate = it },
        tasks = tasks,
        onAddTask = { fecha, texto -> onAddTask(Task(text = texto, date = fecha)) },
        onUpdateTask = onUpdateTask,
        onDeleteTask = onDeleteTask
    )
}
