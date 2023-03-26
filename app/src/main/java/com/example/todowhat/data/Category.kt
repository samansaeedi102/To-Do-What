package com.example.todowhat.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    var name: String,
    @PrimaryKey val id: Int? = null
)
