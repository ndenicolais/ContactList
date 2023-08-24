package com.denicks21.contactlist.composables

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.denicks21.contactlist.ui.theme.DarkGrey
import com.denicks21.contactlist.ui.theme.LightYellow
import com.denicks21.contactlist.ui.theme.DarkYellow
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate

@Composable
fun DatePicker(onDateSelected: (LocalDate) -> Unit) {
    var pickedDate by remember { mutableStateOf(LocalDate.now()) }
    val dateDialogState = rememberMaterialDialogState()

    ExtendedFloatingActionButton(
        text = {
            Row(
                modifier = Modifier
                    .width(100.dp)
                    .height(40.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "Select date",
                    tint = DarkGrey
                )
                Text(
                    text = "Date Picker",
                    color = DarkGrey,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        },
        backgroundColor = DarkYellow,
        onClick = { dateDialogState.show() }
    )
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(
                text = "Ok",
                textStyle = TextStyle(
                    color = if (isSystemInDarkTheme()) LightYellow else DarkGrey
                )
            ) {
                onDateSelected(pickedDate)
            }
            negativeButton(
                text = "Cancel",
                textStyle = TextStyle(
                    color = if (isSystemInDarkTheme()) LightYellow else DarkGrey
                )
            )
        }
    ) {
        datepicker(
            initialDate = LocalDate.now(),
            title = "Select a date",
            colors = DatePickerDefaults.colors(
                headerBackgroundColor = MaterialTheme.colors.primary,
                headerTextColor = if (isSystemInDarkTheme()) DarkGrey else LightYellow,
                calendarHeaderTextColor = MaterialTheme.colors.primary,
                dateActiveBackgroundColor = MaterialTheme.colors.primary,
                dateInactiveBackgroundColor = Color.Transparent,
                dateActiveTextColor = if (isSystemInDarkTheme()) DarkGrey else LightYellow,
                dateInactiveTextColor = MaterialTheme.colors.primary
            )
        ) {
            pickedDate = it
        }
    }
}