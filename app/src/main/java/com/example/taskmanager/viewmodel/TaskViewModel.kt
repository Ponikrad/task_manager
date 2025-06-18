package com.example.taskmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.model.Task
import com.example.taskmanager.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class TaskViewModel : ViewModel() {
    private val repository = TaskRepository()

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    fun fetchTasks() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.getTasks().fold(
                onSuccess = { tasks ->
                    _uiState.update { it.copy(tasks = tasks, isLoading = false) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(error = e.message, isLoading = false) }
                }
            )
        }
    }

    fun addTask(title: String, description: String, priority: Int) {
        if (validateInput(title, description, priority)) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, error = null) }
                val newTask = Task(title = title, description = description, priority = priority)
                repository.createTask(newTask).fold(
                    onSuccess = { task ->
                        val updatedTasks = _uiState.value.tasks.toMutableList().apply {
                            add(task)
                        }
                        _uiState.update { it.copy(tasks = updatedTasks, isLoading = false) }
                    },
                    onFailure = { e ->
                        _uiState.update { it.copy(error = e.message, isLoading = false) }
                    }
                )
            }
        } else {
            _uiState.update { it.copy(error = "Nieprawidłowe dane wejściowe. Sprawdź pola formularza.") }
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.deleteTask(taskId).fold(
                onSuccess = {
                    val updatedTasks = _uiState.value.tasks.filterNot { it.id == taskId }
                    _uiState.update { it.copy(tasks = updatedTasks, isLoading = false) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(error = e.message, isLoading = false) }
                }
            )
        }
    }

    private fun validateInput(title: String, description: String, priority: Int): Boolean {
        return title.isNotBlank() && description.isNotBlank() && priority in 1..10
    }

    init {
        fetchTasks()
    }
}
