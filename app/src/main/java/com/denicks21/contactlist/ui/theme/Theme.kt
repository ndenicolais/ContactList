package com.denicks21.contactlist.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = LightYellow,
    primaryVariant = LightYellow,
    secondary = LightGrey,
    background = LightYellow,
    surface = DarkSurface,
    onPrimary = DarkGrey,
    onSecondary = LightYellow,
    onBackground = DarkGrey,
    onSurface = LightYellow,
    error = DarkRefuse
)

private val LightColorPalette = lightColors(
    primary = DarkGrey,
    primaryVariant = DarkGrey,
    secondary = DarkYellow,
    background = DarkGrey,
    surface = LightSurface,
    onPrimary = LightYellow,
    onSecondary = DarkGrey,
    onBackground = LightYellow,
    onSurface = DarkGrey,
    error = DarkRefuse
)

var localFocusManager: FocusManager? = null

@Composable
fun customFieldColors(): TextFieldColors {
    return TextFieldDefaults.textFieldColors(
        textColor = if (isSystemInDarkTheme()) DarkGrey else LightYellow,
        backgroundColor = if (isSystemInDarkTheme()) LightYellow else DarkGrey,
        cursorColor = if (isSystemInDarkTheme()) DarkGrey else LightYellow,
        disabledIndicatorColor = Transparent,
        errorIndicatorColor = Color.Red,
        focusedIndicatorColor = if (isSystemInDarkTheme()) DarkGrey else LightYellow,
        unfocusedIndicatorColor = Transparent,
        focusedLabelColor = if (isSystemInDarkTheme()) DarkGrey else LightYellow,
        unfocusedLabelColor = if (isSystemInDarkTheme()) DarkGrey else LightYellow,
        placeholderColor = if (isSystemInDarkTheme()) DarkGrey else LightYellow,
    )
}

@Composable
fun ContactListTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}