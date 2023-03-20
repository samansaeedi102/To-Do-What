package com.example.todowhat.data

import kotlinx.coroutines.flow.Flow

class TodoRepositoryImp(
    private val dao: TodoDao
): TodoRepository {
    override suspend fun insertTodo(todo: Todo) {
        dao.insertTodo(todo)
    }

    override suspend fun deleteTodo(todo: Todo) {
        dao.deleteTodo(todo)
    }

    override suspend fun getTodoById(id: Int): Todo? {
        return dao.getTodoById(id)
    }

//    override fun getTodos(category: String): Flow<List<Todo>> {
//        return dao.getTodos(category)
//    }
    override fun getTodos(): Flow<List<Todo>> {
    return dao.getTodos()
    }
}