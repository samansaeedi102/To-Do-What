package com.example.todowhat.ui.theme.todo_list

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.room.Update
import com.example.todowhat.data.Category
import com.example.todowhat.data.Todo
import com.example.todowhat.util.UiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.drop

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TodoListScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: TodoListviewModel = hiltViewModel()
) {
    var todos = viewModel.todos1.collectAsState(initial = emptyList())
    var filteredTodos = listOf<Todo>()
    val scaffoldState = rememberScaffoldState()
    var expanded by remember { mutableStateOf(false) }
    val catList = viewModel.catList.collectAsState(initial = emptyList())
    var selectedItem by remember { mutableStateOf(Category(name = "All")) }
    val openDialog by remember{ mutableStateOf(false) }
    fun filterTodos() {
        filteredTodos = if(selectedItem.name != "All") {
            todos.value.filter {
                it.category == selectedItem.name
            }
        } else{
            todos.value
        }
    }
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when(event) {
                is UiEvent.ShowSnackBar -> {
                    val result = scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                    if(result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(TodoListEvent.OnUndoDeleteClick)
                    }
                }
                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
                 Card(elevation = 3.dp) {
                     Row(
                         modifier = Modifier
                             .fillMaxWidth()
                             .background(color = Color.Blue)
                     ) {
                         Icon(
                             imageVector = Icons.Default.CheckCircle,
                             contentDescription = "Todos",
                             Modifier
                                 .size(50.dp)
                                 .padding(8.dp),
                             tint = Color.White
                         )
                         CategoriesDropdown(catList.value,selectedItem,onCategorySelected = { category ->
                             selectedItem = category
                         },onAddCategory = { category ->
                             //catList += category
                             viewModel.onEvent(TodoListEvent.OnAddCategory(category))
                             selectedItem = Category(category)
                         },
                         viewModel)
                         Spacer(modifier = Modifier.weight(1f))
                         Icon(
                             imageVector = Icons.Default.Search,
                             contentDescription = "Search",
                             Modifier
                                 .size(50.dp)
                                 .padding(8.dp),
                             tint = Color.White
                         )
                     }
                 }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(TodoListEvent.OnAddTodoClick)
                //Log.d(TAG, "$catList injas")
            }) {
                Icon(imageVector = Icons.Default.Add,
                    contentDescription = "Add Todo"
                )
            }
        }
    ) {
        filterTodos()
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredTodos) {todo ->
                TodoItem(
                    todo = todo,
                    onEvent = viewModel::onEvent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.onEvent(TodoListEvent.OnTodoClick(todo))

                        }
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun AddCategoryDialog(
    onAddCategory: (String) -> Unit,
    onDismiss: () -> Unit,
    viewModel: TodoListviewModel
) {
    var category by remember{ mutableStateOf("") }
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Add Category") },
        text = {
            TextField(
                value = category,
                onValueChange = { category = it },
                label = { Text(text = "Category Name") },
                enabled = true
            )
        },
        confirmButton = {
            TextButton(onClick = {
                onAddCategory(category)
                onDismiss()
            }) {
                Text(text = "Add")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = "Cancel")
            }
        }
    )
}
@Composable
fun CategoriesDropdown(
    categories: List<Category>,
    selectedCategory: Category?,
    onCategorySelected: (Category) -> Unit,
    onAddCategory: (String) -> Unit,
    viewModel: TodoListviewModel
) {
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    val dropdownModifier = Modifier.wrapContentSize()

    Box(modifier = dropdownModifier) {
        DropdownMenu(
            modifier = dropdownModifier,
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    },
                    enabled = category != selectedCategory
                ) {
                    Text(text = category.name)
                }
            }
            DropdownMenuItem(onClick = { showDialog = true }) {
                Text(text = "Add Category")
            }
        }
        if (showDialog) {
            AddCategoryDialog(
                onAddCategory = { category ->
                    onAddCategory(category)
                    expanded = false
                },
                onDismiss = { showDialog = false },
                viewModel = viewModel
            )
        }
        Text(
            text = selectedCategory?.name ?: "Select Category",
            modifier = Modifier.clickable(onClick = { expanded = true })
        )
    }
}
