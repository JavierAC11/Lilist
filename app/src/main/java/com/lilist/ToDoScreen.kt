package com.lilist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoScreen() {
    val context = LocalContext.current
    val taskStorage = remember { TaskStorage(context) }

    var tasks by remember { mutableStateOf(taskStorage.loadTasks()) }
    var newTask by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = newTask,
            onValueChange = { newTask = it },
            label = { Text("Nueva tarea") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            if (newTask.isNotBlank()) {
                tasks = tasks + newTask.trim()  // actualizar estado con nueva lista
                taskStorage.saveTasks(tasks)
                newTask = ""
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Agregar")
        }

        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(tasks.size) { index ->
                Text(text = "• ${tasks[index]}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
