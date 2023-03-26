package com.example.todowhat.di

import android.app.Application
import androidx.room.Room
import com.example.todowhat.data.TodoDatabase
import com.example.todowhat.data.TodoRepository
import com.example.todowhat.data.TodoRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTodoDatabase(app: Application): TodoDatabase {
        return Room.databaseBuilder(
            app,
            TodoDatabase::class.java,
            "todo_db"
        ).allowMainThreadQueries().build()
    }

    @Provides
    @Singleton
    fun provideTodoRepository(todoDatabase: TodoDatabase): TodoRepository {
        return TodoRepositoryImp(todoDatabase.dao, todoDatabase.categoryDao)
    }
}