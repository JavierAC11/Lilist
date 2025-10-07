package com.lilist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
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
                tasks = tasks + Task(text = newTask.trim())
                taskStorage.saveTasks(tasks)
                newTask = ""
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Agregar")
        }

        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(tasks.size) { index ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = tasks[index].isDone,
                        onCheckedChange = { checked ->
                            val id = tasks[index].id
                            tasks = tasks.map { task ->
                                if (task.id == id) task.copy(isDone = checked) else task
                            }
                            taskStorage.saveTasks(tasks)
                        }
                    )

                    Text(
                        text = tasks[index].text,
                        style = if (tasks[index].isDone) MaterialTheme.typography.bodyLarge.copy(textDecoration = TextDecoration.LineThrough)
                        else MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = {
                        tasks = tasks.filter { it.id != tasks[index].id }
                        taskStorage.saveTasks(tasks)
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    }

                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
