package com.lilist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.lilist.data.Challenge
import com.lilist.data.ChallengeStorage
import com.lilist.data.Task
import com.lilist.data.TaskStorage
import com.lilist.ui.CalendarScreen
import com.lilist.ui.ChallengeScreen
import com.lilist.ui.ToDoScreen
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val taskStorage = TaskStorage(this)
        val challengeStorage = ChallengeStorage(this)
        setContent {
            MainScreen(taskStorage, challengeStorage)
        }
    }
}

@Composable
fun MainScreen(taskStorage: TaskStorage, challengeStorage: ChallengeStorage) {
    var selectedScreen by remember { mutableStateOf("todo") }

    var tasks by remember { mutableStateOf(taskStorage.loadTasks()) }
    var challenges by remember { mutableStateOf(challengeStorage.loadChallenges()) }

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

    fun agregarReto(reto: Challenge) {
        challenges = challenges + reto
        challengeStorage.saveChallenges(challenges)
    }

    fun eliminarReto(reto: Challenge) {
        challenges = challenges.filter { it.id != reto.id }
        challengeStorage.saveChallenges(challenges)
    }

    fun actualizarReto(nuevoReto: Challenge) {
        challenges = challenges.map { if (it.id == nuevoReto.id) nuevoReto else it }
        challengeStorage.saveChallenges(challenges)
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
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Star, contentDescription = "Retos") },
                    label = { Text("Retos") },
                    selected = selectedScreen == "challenges",
                    onClick = { selectedScreen = "challenges" }
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
                "challenges" -> ChallengeScreen(
                    challenges = challenges,
                    onAddChallenge = ::agregarReto,
                    onDeleteChallenge = ::eliminarReto,
                    onUpdateChallenge = ::actualizarReto
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
