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

    // Carga sin ordenar
    var tasks by remember { mutableStateOf(taskStorage.loadTasks()) }
    var newTask by remember { mutableStateOf("") }

    // Ordena tareas para la UI, sin modificar la persistencia original
    val orderedTasks = remember(tasks) { tasks.sortedWith(compareBy({ it.isDone }, { it.id })) }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = newTask,
            onValueChange = { newTask = it },
            label = { Text("Nueva tarea") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            if (newTask.isNotBlank()) {
                tasks = tasks + Task(text = newTask.trim())
                taskStorage.saveTasks(tasks)
                newTask = ""
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Agregar")
        }
        Spacer(Modifier.height(16.dp))
        LazyColumn {
            items(orderedTasks.size) { index ->
                val task = orderedTasks[index]
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = task.isDone,
                        onCheckedChange = { checked ->
                            tasks = tasks.map { if (it.id == task.id) it.copy(isDone = checked) else it }
                            taskStorage.saveTasks(tasks)
                        }
                    )
                    Text(
                        text = task.text,
                        style = if (task.isDone) MaterialTheme.typography.bodyLarge.copy(textDecoration = TextDecoration.LineThrough)
                        else MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = {
                        tasks = tasks.filter { it.id != task.id }
                        taskStorage.saveTasks(tasks)
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    }
                }
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}
