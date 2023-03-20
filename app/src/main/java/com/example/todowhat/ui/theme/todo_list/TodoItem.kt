package com.example.todowhat.ui.theme.todo_list

import android.widget.CheckBox
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todowhat.data.Todo

@Composable
fun TodoItem(
    todo: Todo,
    onEvent: (TodoListEvent) ->Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = 3.dp,
        modifier = Modifier.padding(15.dp, 7.dp)
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = todo.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                todo.description?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = it)
                }
                Text(text = todo.category!!)
            }
            Column() {
                Checkbox(
                    checked = todo.isDone,
                    onCheckedChange = { isChecked ->
                        onEvent(TodoListEvent.OnDoneChange(todo, isChecked))
                    })
                IconButton(onClick = {
                    onEvent(TodoListEvent.OnDeleteTodoClick(todo))
                }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}