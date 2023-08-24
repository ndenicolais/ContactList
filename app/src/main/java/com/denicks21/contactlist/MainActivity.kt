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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.denicks21.contactlist.activities.EditContactActivity
import com.denicks21.contactlist.activities.InfoActivity
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
                            navigateToInfoPage()
                        }
                    ),
                    numIcons = 1
                )
            },
            backgroundColor = MaterialTheme.colors.primary
        )
    }

    private fun navigateToContactPage(contacts: Contacts? = null) {
        val intent = Intent(this, EditContactActivity::class.java)
        intent.putExtra("contacts", contacts)
        startActivity(intent)
    }

    private fun navigateToInfoPage() {
        val intent = Intent(this, InfoActivity::class.java)
        startActivity(intent)
    }
}