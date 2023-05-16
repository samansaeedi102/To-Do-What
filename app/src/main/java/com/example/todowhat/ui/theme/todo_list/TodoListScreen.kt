package com.example.todowhat.ui.theme.todo_list

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todowhat.R
import com.example.todowhat.data.Category
import com.example.todowhat.data.Todo
import com.example.todowhat.ui.theme.AppTop
import com.example.todowhat.ui.theme.Black
import com.example.todowhat.ui.theme.Gray
import com.example.todowhat.ui.theme.OnCard
import com.example.todowhat.util.SearchTextField
import com.example.todowhat.util.UiEvent

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TodoListScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: TodoListviewModel = hiltViewModel()
) {
    var todos = viewModel.todos.collectAsState(initial = emptyList())
    var filteredTodos = listOf<Todo>()
    val scaffoldState = rememberScaffoldState()
    val catList = viewModel.catList.collectAsState(initial = emptyList())
    var selectedItem = viewModel.selectedItem
    val uiState by viewModel.uiState.collectAsState()
    val searchTextFieldFocusRequester = remember { FocusRequester() }
    if(viewModel.searchClicked) {
        LaunchedEffect(Unit) {
            searchTextFieldFocusRequester.requestFocus()
        }
    }
    fun filteredTodos() {
        filteredTodos = if(selectedItem != "All") {
            todos.value.filter {
                it.category == selectedItem
            }
        } else{
            todos.value
        }
    }
    fun filterByTitle() {
        filteredTodos = todos.value.filter {
            it.title.contains(uiState.currentSearchedTerm)
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
                Card(elevation = 3.dp, modifier = Modifier.fillMaxHeight(0.07f)) {
                     if(!viewModel.searchClicked) {
                         Row(
                             modifier = Modifier
                                 .fillMaxWidth()
                                 .background(color = AppTop),
                             verticalAlignment = Alignment.CenterVertically
                         ) {
                             Icon(
                                     imageVector = Icons.Default.CheckCircle,
                                     contentDescription = "Todos",
                                 Modifier
                                     .size(50.dp)
                                     .padding(8.dp),
                                     tint = Color.Black
                             )
                             CategoriesDropdown(catList.value,selectedItem,onCategorySelected = { category ->
                                 viewModel.onEvent(TodoListEvent.OnCategorySelect(category = category))
                             },onAddCategory = { category ->
                                 viewModel.onEvent(TodoListEvent.OnAddCategory(category))
                                 selectedItem = Category(category).name
                             },
                                 viewModel)
                             Spacer(modifier = Modifier.weight(1f))
                             IconButton(onClick = {
                                 viewModel.onEvent(TodoListEvent.OnSearchClick)
                             }) {
                                 Icon(
                                     imageVector = Icons.Default.Search,
                                     contentDescription = "Search",
                                     Modifier
                                         .size(50.dp)
                                         .padding(8.dp),
                                     tint = Color.Black
                                 )
                             }
                             OptionMenu()
                         }
                     } else {
                         selectedItem = "All"
                         Row(
                             modifier = Modifier
                                 .fillMaxWidth()
                                 .background(color = AppTop),
                             verticalAlignment = Alignment.CenterVertically
                         ) {
                             IconButton(onClick = {
                                 viewModel.onEvent(TodoListEvent.OnSearchClick)
                                 selectedItem = "Personal"

                             }) {
                                 Icon(imageVector = Icons.Default.ArrowBack,
                                     contentDescription = "cancel search",
                                     tint = Black
                                 )
                             }
                             Spacer(modifier = Modifier.width(20.dp))
                             SearchTextField(
                                 value = uiState.currentSearchedTerm ,
                                 onCloseClick = { viewModel.onEvent(TodoListEvent.OnSearchClick) },
                                 onValueChange = {
                                     viewModel.onEvent(TodoListEvent.OnSearch(it))
                                                 },
                                 keyboardOptions = KeyboardOptions(
                                     keyboardType = KeyboardType.Text,
                                     imeAction = ImeAction.Search
                                 ),
                                 keyboardActions = KeyboardActions(
                                     onSearch = {}
                                 ),
                                 focusRequester = searchTextFieldFocusRequester,
                                 icon = Icons.Default.Search
                             )
                         }
                     }
                 }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(TodoListEvent.OnAddTodoClick)
            }) {
                Icon(imageVector = Icons.Default.Add,
                    contentDescription = "Add Todo"
                )
            }
        }
    ) {
        if(viewModel.searchClicked) {
            filterByTitle()
            if(filteredTodos.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${uiState.currentSearchedTerm} not found",
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                        fontSize = 25.sp
                    )
                }
            } else {
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
        } else {
            filteredTodos()
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
                enabled = true,
                placeholder = { Text(text = "Enter new category")},
                leadingIcon = {Icon(imageVector = Icons.Default.Add, contentDescription = "search", tint = Black)},
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    backgroundColor =  Gray
                )

            )
        },
        confirmButton = {
            TextButton(onClick = {
                onAddCategory(category)
                viewModel.onEvent(TodoListEvent.OnCategorySelect(category = category))
                onDismiss()
            }) {
                Text(text = "Add", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = "Cancel", color = Color.White)
            }
        }
    )
}
@Composable
fun CategoriesDropdown(
    categories: List<Category>,
    selectedCategory: String?,
    onCategorySelected: (String) -> Unit,
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
                        onCategorySelected(category.name)
                        expanded = false
                    },
                    enabled = category.name != selectedCategory
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
            text = selectedCategory ?: "Select Category",
            modifier = Modifier.clickable(onClick = { expanded = true }),
            color = Black
        )
    }
}

@Composable
fun OptionMenu() {
    var showMenu by remember { mutableStateOf(false) }

        Box(modifier = Modifier) {
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(Icons.Default.MoreVert, "", tint = Black)
            }
            DropdownMenu(expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(onClick = { /*TODO*/ }) {
                    Text(text = "Categories")
                }
            }
        }
}
