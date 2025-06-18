package com.example.taskmanager.repository

import com.example.taskmanager.api.RetrofitClient
import com.example.taskmanager.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun getTasks(): Result<List<Task>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getTasks()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Błąd: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createTask(task: Task): Result<Task> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.createTask(task)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Błąd: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteTask(taskId: Int): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.deleteTask(taskId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Błąd: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}