package com.example.taskmanager.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskmanager.R
import com.example.taskmanager.model.Task
import com.example.taskmanager.viewmodel.TaskViewModel

@Composable
fun TaskManagerApp() {
    val taskViewModel: TaskViewModel = viewModel()
    val uiState by taskViewModel.uiState.collectAsState()

    TaskManagerScreen(
        tasks = uiState.tasks,
        isLoading = uiState.isLoading,
        error = uiState.error,
        onAddTask = { title, description, priority ->
            taskViewModel.addTask(title, description, priority)
        },
        onDeleteTask = { taskId ->
            taskViewModel.deleteTask(taskId)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskManagerScreen(
    tasks: List<Task>,
    isLoading: Boolean,
    error: String?,
    onAddTask: (String, String, Int) -> Unit,
    onDeleteTask: (Int) -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priorityText by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.app_logo),
                            contentDescription = "Logo aplikacji",
                            modifier = Modifier.size(40.dp)
                        )
                        Text(
                            text = "Task Manager",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Dodaj zadanie")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Column(modifier = Modifier.fillMaxSize()) {
                if (error != null) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = error,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    if (tasks.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Brak zadań. Dodaj nowe zadanie.")
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(tasks) { task ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = task.title,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 18.sp
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(text = task.description)
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                val color = when {
                                                    task.priority <= 3 -> Color(0xFF4CAF50)
                                                    task.priority <= 7 -> Color(0xFFFFC107)
                                                    else -> Color(0xFFF44336)
                                                }
                                                Box(
                                                    modifier = Modifier
                                                        .size(16.dp)
                                                        .background(color, shape = RoundedCornerShape(4.dp))
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "Priorytet: ${task.priority}",
                                                    color = Color.DarkGray,
                                                    fontSize = 14.sp
                                                )
                                            }
                                        }

                                        IconButton(onClick = { onDeleteTask(task.id) }) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Usuń zadanie",
                                                tint = MaterialTheme.colorScheme.error
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Dodaj nowe zadanie") },
                    text = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = title,
                                onValueChange = { title = it },
                                label = { Text("Tytuł") },
                                isError = title.isBlank(),
                                supportingText = { if (title.isBlank()) Text("Tytuł nie może być pusty") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = description,
                                onValueChange = { description = it },
                                label = { Text("Opis") },
                                isError = description.isBlank(),
                                supportingText = { if (description.isBlank()) Text("Opis nie może być pusty") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = priorityText,
                                onValueChange = { priorityText = it },
                                label = { Text("Priorytet (1-10)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                isError = priorityText.toIntOrNull() !in 1..10,
                                supportingText = {
                                    if (priorityText.toIntOrNull() !in 1..10)
                                        Text("Priorytet musi być liczbą od 1 do 10")
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                val priority = priorityText.toIntOrNull() ?: 0
                                if (title.isNotBlank() && description.isNotBlank() && priority in 1..10) {
                                    onAddTask(title, description, priority)
                                    title = ""
                                    description = ""
                                    priorityText = ""
                                    showDialog = false
                                }
                            }
                        ) {
                            Text("Dodaj")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("Anuluj")
                        }
                    }
                )
            }
        }
    }
}