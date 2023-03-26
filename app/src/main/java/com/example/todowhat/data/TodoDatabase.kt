package com.example.todowhat.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Todo::class, Category::class],
    version = 2
)
abstract class TodoDatabase: RoomDatabase() {
    abstract val dao: TodoDao
    abstract val categoryDao: CategoryDao
}