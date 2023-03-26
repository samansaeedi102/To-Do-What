package com.example.todowhat.ui.theme.todo_list

import com.example.todowhat.data.Category
import com.example.todowhat.data.Todo

sealed class TodoListEvent {
    data class OnDeleteTodoClick(val todo: Todo): TodoListEvent()
    data class OnDoneChange(val todo: Todo, val isDone: Boolean): TodoListEvent()
    object OnUndoDeleteClick: TodoListEvent()
    data class OnTodoClick(val todo: Todo): TodoListEvent()
    object OnAddTodoClick: TodoListEvent()
    data class OnCategorySelect(var category: String): TodoListEvent()
    data class OnAddCategory(var category: String): TodoListEvent()
}
