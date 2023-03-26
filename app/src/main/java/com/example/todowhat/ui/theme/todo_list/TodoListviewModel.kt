package com.example.todowhat.ui.theme.todo_list

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todowhat.data.Category
import com.example.todowhat.data.Todo
import com.example.todowhat.data.TodoRepository
import com.example.todowhat.util.Routes
import com.example.todowhat.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListviewModel @Inject constructor(
    private val todoRepository: TodoRepository
): ViewModel() {
    var category by mutableStateOf("All")
        private set
    private var todos = todoRepository.getTodos()
    var todos1 = todoRepository.getTodos()
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()
    private var deletedTodo: Todo? = null
    var catList = todoRepository.getCategories()

    init {
        viewModelScope.launch {
            todoRepository.insertCategory(
                Category(
                    name = "All",
                    id = 0
                )
            )
            todoRepository.insertCategory(
                Category(
                    name = "Personal",
                    id = 1
                )
            )
            todoRepository.insertCategory(
                Category(
                    name = "Shopping",
                    id = 2
                )
            )
            todoRepository.insertCategory(
                Category(
                    name = "Wishlist",
                    id = 3
                )
            )
            todoRepository.insertCategory(
                Category(
                    name = "Work",
                    id = 4
                )
            )
        }

    }
    fun onEvent(event: TodoListEvent) {
        when(event) {
            is TodoListEvent.OnTodoClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO + "?todoId=${event.todo.id}"))
            }
            is TodoListEvent.OnAddTodoClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO))
            }
            is TodoListEvent.OnUndoDeleteClick -> {
                deletedTodo?.let { todo ->
                    viewModelScope.launch {
                        todoRepository.insertTodo(todo)
                        todos = todoRepository.getTodos()
                    }
                }
            }
            is TodoListEvent.OnDeleteTodoClick -> {
                deletedTodo = event.todo
                viewModelScope.launch {
                    todoRepository.deleteTodo(event.todo)
                    sendUiEvent(UiEvent.ShowSnackBar(
                        message = "Todo Deleted",
                        action = "Undo"
                    ))
                }
            }
            is TodoListEvent.OnDoneChange -> {
                viewModelScope.launch {
                    todoRepository.insertTodo(
                        event.todo.copy(
                            isDone = event.isDone
                        )
                    )
                }
            }
            is TodoListEvent.OnCategorySelect -> {
                viewModelScope.launch {
                    category = event.category
                    todos = todoRepository.getTodos()
                }
            }
            is TodoListEvent.OnAddCategory -> {
                viewModelScope.launch {
                    todoRepository.insertCategory(
                        category = Category(
                            name = event.category,

                        )
                    )
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}