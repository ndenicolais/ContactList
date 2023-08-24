package com.denicks21.contactlist

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAddAlt
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.denicks21.contactlist.activities.EditContactActivity
import com.denicks21.contactlist.composables.ActionItem
import com.denicks21.contactlist.composables.ActionMenu
import com.denicks21.contactlist.composables.CustomAlertDialog
import com.denicks21.contactlist.composables.OverflowMode
import com.denicks21.contactlist.roomdb.Contacts
import com.denicks21.contactlist.ui.theme.*
import com.denicks21.contactlist.viewmodels.ContactViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    private val contactViewModel: ContactViewModel by lazy {
        ContactViewModel(this, this)
    }
    private var deleteContact: Contacts? by mutableStateOf(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContactListTheme {
                val systemUiController = rememberSystemUiController()
                val statusBarColor = MaterialTheme.colors.surface
                val navigationBarColor = MaterialTheme.colors.onSurface
                val barIcons = isSystemInDarkTheme()

                SideEffect {
                    systemUiController.setNavigationBarColor(
                        color = navigationBarColor,
                        darkIcons = barIcons
                    )
                    systemUiController.setStatusBarColor(
                        color = statusBarColor,
                        darkIcons = true
                    )
                }
                MainPage()
            }
        }
    }

    @Composable
    fun MainPage() {
        LaunchedEffect(key1 = Unit) {
            contactViewModel.getAllContacts()
        }
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.surface
        ) {
            Column(verticalArrangement = Arrangement.Top) {
                TopBar()
                SetAddresses()

                deleteContact?.run {
                    CustomAlertDialog(
                        title = "Delete contact",
                        text = "Are you sure want to delete this contact?",
                        confirmButtonText = "Yes",
                        dismissButtonText = "No",
                    ) { isPositive ->
                        if (isPositive) {
                            contactViewModel.deleteContact(this)
                        }
                        deleteContact = null
                    }
                }
            }
        }
    }

    @Composable
    private fun SetAddresses() {
        contactViewModel.run {
            if (mutableContact.size == 0) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ViewAgenda,
                        contentDescription = "Empty list",
                        modifier = Modifier.size(120.dp),
                        tint = if (isSystemInDarkTheme()) LightYellow else DarkGrey
                    )
                }
                return
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                items(contactViewModel.mutableContact) { contacts ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        backgroundColor = MaterialTheme.colors.background
                    ) {
                        Column(modifier = Modifier.padding(15.dp)) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = contacts.name + " " + contacts.surname,
                                    color = if (isSystemInDarkTheme()) DarkGrey else LightYellow,
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                if (contacts.image != null && contacts.image!!.isNotEmpty()) {
                                    Image(
                                        painter = rememberAsyncImagePainter(
                                            model = contacts.image
                                        ),
                                        contentDescription = "User image",
                                        alignment = Alignment.Center,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(200.dp)
                                            .clip(CircleShape)
                                            .border(
                                                width = 1.dp,
                                                color = if (isSystemInDarkTheme()) DarkGrey else LightYellow,
                                                shape = CircleShape
                                            )
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        modifier = Modifier.size(120.dp),
                                        tint = if (isSystemInDarkTheme()) DarkGrey else LightYellow
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Divider(
                                thickness = 1.dp,
                                color = if (isSystemInDarkTheme()) DarkGrey else LightYellow
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = contacts.email ?: "-",
                                color = if (isSystemInDarkTheme()) DarkGrey else LightYellow,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = contacts.phoneNumber ?: "-",
                                color = if (isSystemInDarkTheme()) DarkGrey else LightYellow,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "${
                                    contacts.birthdate?.format(
                                        DateTimeFormatter.ofPattern(
                                            "dd/MM/yyyy"
                                        )
                                    )
                                }",
                                color = if (isSystemInDarkTheme()) DarkGrey else LightYellow,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )
                            Row(modifier = Modifier.align(Alignment.End)) {
                                IconButton(
                                    modifier = Modifier.size(24.dp),
                                    onClick = { navigateToContactPage(contacts) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = "Edit",
                                        tint = if (isSystemInDarkTheme()) DarkGrey else LightYellow
                                    )
                                }
                                IconButton(
                                    modifier = Modifier
                                        .padding(start = 16.dp)
                                        .size(24.dp),
                                    onClick = { deleteContact = contacts }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "Delete",
                                        tint = DarkRefuse
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun TopBar() {
        var showInfoDialog by remember { mutableStateOf(false) }
        TopAppBar(
            title = {
                Text(
                    text = "Contact List",
                    color = if (isSystemInDarkTheme()) DarkText else LightText,
                )
            },
            modifier = Modifier.fillMaxWidth(),
            actions = {
                ActionMenu(
                    items = listOf(
                        ActionItem(
                            nameRes = "Add new contact",
                            icon = Icons.Filled.PersonAddAlt,
                            overflowMode = OverflowMode.NEVER_OVERFLOW
                        ) {
                            navigateToContactPage()
                        },
                        ActionItem(
                            nameRes = "Info page",
                            icon = Icons.Filled.Info,
                            overflowMode = OverflowMode.NEVER_OVERFLOW
                        ) {
                            showInfoDialog = true
                        }
                    ),
                    numIcons = 1
                )
            },
            backgroundColor = MaterialTheme.colors.primary
        )
        if (showInfoDialog) {
            val uriHandler = LocalUriHandler.current

            Dialog(
                onDismissRequest = { showInfoDialog = false }
            ) {
                Card(
                    modifier = Modifier
                        .wrapContentHeight()
                        .height(470.dp)
                        .width(450.dp),
                    shape = RoundedCornerShape(size = 8.dp),
                    backgroundColor = MaterialTheme.colors.background
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        IconButton(
                            onClick = { showInfoDialog = false },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Close dialog"
                            )
                        }
                        Card(
                            modifier = Modifier
                                .height(400.dp)
                                .width(450.dp)
                                .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
                            shape = RoundedCornerShape(8.dp),
                            backgroundColor = MaterialTheme.colors.onBackground,
                            elevation = 10.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = stringResource(id = R.string.app_name),
                                    color = if (isSystemInDarkTheme()) LightText else DarkText,
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Image(
                                    painter = painterResource(id = R.drawable.logo),
                                    contentDescription = "Logo",
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .border(
                                            width = 1.dp,
                                            color = if (isSystemInDarkTheme()) LightText else DarkText,
                                            shape = CircleShape
                                        )
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                Divider(
                                    color = if (isSystemInDarkTheme()) LightText else DarkText,
                                    thickness = 2.dp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Android application built with Kotlin and Jetpack Compose that shows " +
                                            "how to perform CRUD operations in the Room database using Android Architecture Components " +
                                            "and the MVVM Architecture Pattern.",
                                    color = if (isSystemInDarkTheme()) LightText else DarkText,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Divider(
                                    color = if (isSystemInDarkTheme()) LightText else DarkText,
                                    thickness = 2.dp
                                )
                                Spacer(modifier = Modifier.height(15.dp))
                                Text(
                                    text = "My GitHub",
                                    color = if (isSystemInDarkTheme()) LightText else DarkText,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 30.sp
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    IconButton(
                                        onClick = { uriHandler.openUri("https://github.com/ndenicolais") },
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.github_logo),
                                            contentDescription = "Open Github",
                                            colorFilter = ColorFilter.tint(if (isSystemInDarkTheme()) LightText else DarkText)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 10.dp),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Developed by DeNicks21",
                            color = if (isSystemInDarkTheme()) DarkText else LightText,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }

    private fun navigateToContactPage(contacts: Contacts? = null) {
        val intent = Intent(this, EditContactActivity::class.java)
        intent.putExtra("contacts", contacts)
        startActivity(intent)
    }
}