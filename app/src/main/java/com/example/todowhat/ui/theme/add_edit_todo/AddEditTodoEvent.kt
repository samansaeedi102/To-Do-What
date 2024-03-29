package com.example.todowhat.ui.theme.add_edit_todo

sealed class AddEditTodoEvent {
    data class OnTitleChange(val title: String): AddEditTodoEvent()
    data class OnDescriptionChange(val description: String): AddEditTodoEvent()
    data class OnCategoryChange(val category: String): AddEditTodoEvent()
    object OnSaveTodoClick: AddEditTodoEvent()
}
