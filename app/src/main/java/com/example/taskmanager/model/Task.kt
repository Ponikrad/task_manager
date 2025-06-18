package com.example.taskmanager.model

import com.google.gson.annotations.SerializedName

data class Task(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("priority") val priority: Int
)