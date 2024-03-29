package com.example.todowhat.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(
    val title: String,
    val description: String?,
    val isDone: Boolean,
    @PrimaryKey val id: Int? = null,
    var category: String? = "All",
    val allCat: String = "All"

)
