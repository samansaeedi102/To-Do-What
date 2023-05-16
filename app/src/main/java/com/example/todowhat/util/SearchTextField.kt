package com.example.todowhat.util

import android.media.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todowhat.ui.theme.AppTop
import com.example.todowhat.ui.theme.Black

@Composable
fun SearchTextField(
    value: String,
    onCloseClick: () -> Unit,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    focusRequester: FocusRequester = FocusRequester(),
    icon: ImageVector
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle.Default.copy(fontSize = 12.sp),
        leadingIcon = {Icon(imageVector = icon, contentDescription = "search", tint = Black)},
        modifier = Modifier
            .fillMaxWidth()
            .height(49.dp)
            .clip(RoundedCornerShape(10.dp))
            .focusRequester(focusRequester),
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            disabledTextColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            backgroundColor =  AppTop
        )
    )
}