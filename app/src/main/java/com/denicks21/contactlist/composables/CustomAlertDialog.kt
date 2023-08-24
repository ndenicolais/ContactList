package com.denicks21.contactlist.composables

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.denicks21.contactlist.ui.theme.*

@Composable
fun CustomAlertDialog(
    title: String,
    text: String,
    confirmButtonText: String,
    dismissButtonText: String,
    onClick: (isPositive: Boolean) -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onClick(false) },
        confirmButton = {
            TextButton(
                onClick = { onClick(true) },
                modifier = Modifier
                    .clip(CircleShape)
                    .padding(bottom = 5.dp)
                    .size(50.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isSystemInDarkTheme()) LightConfirm else DarkConfirm
                )
            ) {
                Text(
                    text = confirmButtonText,
                    color = if (isSystemInDarkTheme()) Color.Black else Color.White
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onClick(false) },
                modifier = Modifier
                    .clip(CircleShape)
                    .padding(bottom = 5.dp, end = 5.dp)
                    .size(50.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isSystemInDarkTheme()) LightRefuse else DarkRefuse
                )
            ) {
                Text(
                    text = dismissButtonText,
                    color = Color.White
                )
            }
        },
        title = {
            Text(
                text = title,
                color = if (isSystemInDarkTheme()) LightText else DarkText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = text,
                color = if (isSystemInDarkTheme()) LightText else DarkText,
                fontSize = 16.sp,
            )
        }
    )
}