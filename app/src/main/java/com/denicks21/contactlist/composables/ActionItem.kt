package com.denicks21.contactlist.composables

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.denicks21.contactlist.ui.theme.DarkGrey
import com.denicks21.contactlist.ui.theme.DarkText
import com.denicks21.contactlist.ui.theme.DarkYellow
import com.denicks21.contactlist.ui.theme.LightText

data class ActionItem(
    val nameRes: String,
    val icon: ImageVector? = null,
    val overflowMode: OverflowMode = OverflowMode.IF_NECESSARY,
    val doAction: () -> Unit,
) {
    operator fun invoke() = doAction()
}

enum class OverflowMode {
    NEVER_OVERFLOW, IF_NECESSARY, ALWAYS_OVERFLOW, NOT_SHOWN
}

@Composable
fun ActionMenu(
    items: List<ActionItem>,
    numIcons: Int = 3,
    menuVisible: MutableState<Boolean> = remember { mutableStateOf(false) },
) {
    if (items.isEmpty()) {
        return
    }

    val (appbarActions, overflowActions) = remember(items, numIcons) {
        separateIntoIconAndOverflow(items = items, numIcons = numIcons)
    }

    for (item in appbarActions) {
        key(item.hashCode()) {
            val name = item.nameRes
            if (item.icon != null) {
                IconButton(
                    onClick = item.doAction
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = name,
                        tint = if (isSystemInDarkTheme()) DarkText else LightText
                    )
                }
            } else {
                TextButton(
                    onClick = item.doAction
                ) {
                    Text(
                        text = name,
                        color = if (isSystemInDarkTheme()) DarkText else LightText
                    )
                }
            }
        }
    }

    if (overflowActions.isNotEmpty()) {
        IconButton(
            onClick = { menuVisible.value = true }
        ) {
            Icon(
                Icons.Default.MoreVert,
                contentDescription = "More actions",
                tint = if (isSystemInDarkTheme()) DarkText else LightText
            )
        }
        DropdownMenu(
            expanded = menuVisible.value,
            onDismissRequest = { menuVisible.value = false },
        ) {
            for (item in overflowActions) {
                key(item.hashCode()) {
                    DropdownMenuItem(
                        onClick = {
                            menuVisible.value = false
                            item.doAction()
                        }
                    ) {
                        //Icon(item.icon, item.name) just have text in the overflow menu
                        Text(item.nameRes)
                    }
                }
            }
        }
    }
}

private fun separateIntoIconAndOverflow(
    items: List<ActionItem>, numIcons: Int,
): Pair<List<ActionItem>, List<ActionItem>> {
    var (iconCount, overflowCount, preferIconCount) = Triple(0, 0, 0)
    for (item in items) {
        when (item.overflowMode) {
            OverflowMode.NEVER_OVERFLOW -> iconCount++
            OverflowMode.IF_NECESSARY -> preferIconCount++
            OverflowMode.ALWAYS_OVERFLOW -> overflowCount++
            OverflowMode.NOT_SHOWN -> {}
        }
    }

    val needsOverflow = iconCount + preferIconCount > numIcons || overflowCount > 0
    val actionIconSpace = numIcons - (if (needsOverflow) 1 else 0)
    val iconActions = ArrayList<ActionItem>()
    val overflowActions = ArrayList<ActionItem>()
    var iconsAvailableBeforeOverflow = actionIconSpace - iconCount

    for (item in items) {
        when (item.overflowMode) {
            OverflowMode.NEVER_OVERFLOW -> {
                iconActions.add(item)
            }
            OverflowMode.ALWAYS_OVERFLOW -> {
                overflowActions.add(item)
            }
            OverflowMode.IF_NECESSARY -> {
                if (iconsAvailableBeforeOverflow > 0) {
                    iconActions.add(item)
                    iconsAvailableBeforeOverflow--
                } else {
                    overflowActions.add(item)
                }
            }
            OverflowMode.NOT_SHOWN -> {
                // skip
            }
        }
    }
    return Pair(iconActions, overflowActions)
}