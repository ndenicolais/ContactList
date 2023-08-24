package com.denicks21.contactlist.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.denicks21.contactlist.ui.theme.customFieldColors
import com.denicks21.contactlist.ui.theme.localFocusManager

@Composable
fun CustomTextField(
    value: String,
    label: String,
    isError: Boolean,
    imeAction: ImeAction,
    keyboardType: KeyboardType,
    capitalization: KeyboardCapitalization,
    onValueChangeListener: (value: String) -> Unit,
) {
    TextField(
        value = value,
        onValueChange = { onValueChangeListener.invoke(it) },
        modifier = Modifier.fillMaxWidth(0.87f),
        label = { Text(text = label) },
        isError = isError,
        keyboardOptions = KeyboardOptions(imeAction = imeAction, keyboardType = keyboardType, capitalization = capitalization),
        singleLine = true,
        keyboardActions = KeyboardActions(onDone = { localFocusManager?.clearFocus() }),
        shape = RoundedCornerShape(percent = 16),
        colors = customFieldColors()
    )
}