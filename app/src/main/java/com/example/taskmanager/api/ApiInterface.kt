package com.example.taskmanager.api

import com.example.taskmanager.model.Task
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {
    @GET("tasks")
    suspend fun getTasks(): Response<List<Task>>

    @POST("tasks")
    suspend fun createTask(@Body task: Task): Response<Task>

    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") taskId: Int): Response<Unit>
}