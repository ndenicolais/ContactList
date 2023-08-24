package com.denicks21.contactlist.activities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.denicks21.contactlist.roomdb.Contacts
import com.denicks21.contactlist.composables.CustomAlertDialog
import com.denicks21.contactlist.composables.CustomTextField
import com.denicks21.contactlist.composables.DatePicker
import com.denicks21.contactlist.composables.ImagePicker
import com.denicks21.contactlist.ui.theme.*
import com.denicks21.contactlist.utils.Validation
import com.denicks21.contactlist.viewmodels.ContactViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class EditContactActivity : ComponentActivity() {
    private var contacts: Contacts? = null
    private var nameError by mutableStateOf(false)
    private var surnameError by mutableStateOf(false)
    private var emailError by mutableStateOf(false)
    private var phoneNumberError by mutableStateOf(false)
    private var showUpdateAlertDialog by mutableStateOf(false)
    private var showDeleteAlertDialog by mutableStateOf(false)
    private val contactViewModel: ContactViewModel by lazy {
        ContactViewModel(this, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contacts = getIntentData()

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

    private fun getIntentData(): Contacts? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("contacts", Contacts::class.java)
        } else {
            intent.getSerializableExtra("contacts").let {
                if (it is Contacts) {
                    it
                } else null
            }
        }
    }

    @Composable
    fun MainPage() {
        val scrollState = rememberScrollState()

        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.verticalScroll(scrollState),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var image by remember { mutableStateOf(contacts?.image ?: byteArrayOf()) }
                var name by remember { mutableStateOf(contacts?.name ?: "") }
                var surname by remember { mutableStateOf(contacts?.surname ?: "") }
                var city by remember { mutableStateOf(contacts?.city ?: "") }
                var email by remember { mutableStateOf(contacts?.email ?: "") }
                var phoneNumber by remember { mutableStateOf(contacts?.phoneNumber ?: "") }
                var birthdate by remember { mutableStateOf(contacts?.birthdate ?: LocalDate.now()) }

                fun ByteArray.toBitmap(): Bitmap? {
                    return BitmapFactory.decodeByteArray(this, 0, this.size)
                }

                TopBar()
                Spacer(modifier = Modifier.height(16.dp))
                ImagePicker(
                    initialImage = contacts?.image?.toBitmap(),
                    onPhotoSelected = { selectedBitmap ->
                        val stream = ByteArrayOutputStream()
                        selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
                        image = stream.toByteArray()
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomTextField(
                    value = name,
                    label = "Insert name",
                    isError = nameError,
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences
                ) {
                    name = it
                    nameError = false
                }
                Spacer(modifier = Modifier.height(16.dp))
                CustomTextField(
                    value = surname,
                    label = "Insert surname",
                    isError = surnameError,
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences

                ) {
                    surname = it
                    surnameError = false
                }
                Spacer(modifier = Modifier.height(16.dp))
                CustomTextField(
                    value = city,
                    label = "Insert city",
                    isError = false,
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences
                ) {
                    city = it
                }
                Spacer(modifier = Modifier.height(16.dp))
                CustomTextField(
                    value = email,
                    label = "Insert email",
                    isError = emailError,
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email,
                    capitalization = KeyboardCapitalization.None
                ) {
                    email = it
                    emailError = false
                }
                Spacer(modifier = Modifier.height(16.dp))
                CustomTextField(
                    value = phoneNumber,
                    label = "Insert phone number",
                    isError = phoneNumberError,
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number,
                    capitalization = KeyboardCapitalization.Sentences
                ) {
                    phoneNumber = it
                    phoneNumberError = false
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .background(
                            color = if (isSystemInDarkTheme()) LightYellow else DarkGrey,
                            shape = RoundedCornerShape(percent = 16)
                        )
                        .fillMaxWidth(0.87f)
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(130.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${
                            birthdate?.format(
                                DateTimeFormatter.ofPattern(
                                    "dd/MM/yyyy"
                                )
                            )
                        }",
                        color = if (isSystemInDarkTheme()) DarkGrey else LightYellow,
                        fontSize = 16.sp
                    )
                    DatePicker(
                        onDateSelected = { selectedDate ->
                            birthdate = selectedDate
                        }
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                if (contacts != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        FloatingActionButton(
                            onClick = { showDeleteAlertDialog = true },
                            backgroundColor = if (isSystemInDarkTheme()) LightRefuse else DarkRefuse,
                            contentColor = Color.White
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Delete contact"
                            )
                        }
                        FloatingActionButton(
                            onClick = {
                                showUpdateAlertDialog =
                                    isEntriesValid(
                                        image,
                                        name,
                                        surname,
                                        city,
                                        email,
                                        phoneNumber,
                                        birthdate
                                    )
                            },
                            backgroundColor = if (isSystemInDarkTheme()) LightConfirm else DarkConfirm,
                            contentColor = Color.White
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Save,
                                contentDescription = "Update contact"
                            )
                        }
                    }
                } else {
                    FloatingActionButton(
                        onClick = {
                            if (!isEntriesValid(
                                    image,
                                    name,
                                    surname,
                                    city,
                                    email,
                                    phoneNumber,
                                    birthdate
                                )
                            )
                                return@FloatingActionButton

                            val contacts = Contacts(
                                image = image,
                                name = name,
                                surname = surname,
                                city = city,
                                email = email,
                                phoneNumber = phoneNumber,
                                birthdate = birthdate
                            )
                            contactViewModel.insertContact(contacts).also {
                                finish()
                            }

                        },
                        backgroundColor = if (isSystemInDarkTheme()) LightConfirm else DarkConfirm,
                        contentColor = Color.White
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Save,
                            contentDescription = "Save contact"
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                if (showUpdateAlertDialog) {
                    CustomAlertDialog(
                        title = "Update Contact",
                        text = "Do you want to update this contact?",
                        confirmButtonText = "Yes",
                        dismissButtonText = "No",
                        onClick = { isPositive ->
                            showUpdateAlertDialog = false

                            if (isPositive) {
                                val update = Contacts(
                                    id = contacts?.id,
                                    image = image,
                                    name = name,
                                    surname = surname,
                                    city = city,
                                    email = email,
                                    phoneNumber = phoneNumber,
                                    birthdate = birthdate,
                                    isActive = contacts?.isActive
                                )
                                contactViewModel.updateContact(update).also {
                                    finish()
                                }
                            }
                        }
                    )
                }

                if (showDeleteAlertDialog) {
                    CustomAlertDialog(
                        title = "Delete Contact",
                        text = "Do you want to delete this contact?",
                        confirmButtonText = "Yes",
                        dismissButtonText = "No",
                        onClick = { isPositive ->
                            showDeleteAlertDialog = false

                            if (isPositive) {
                                val delete = Contacts(
                                    id = contacts?.id,
                                    image = image,
                                    name = name,
                                    surname = surname,
                                    city = city,
                                    email = email,
                                    phoneNumber = phoneNumber,
                                    birthdate = birthdate,
                                    isActive = contacts?.isActive
                                )
                                contactViewModel.deleteContact(delete).also {
                                    finish()
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun TopBar() {
        TopAppBar(
            title = {
                Text(
                    text = "Contact Info",
                    color = if (isSystemInDarkTheme()) DarkText else LightText
                )
            },
            modifier = Modifier.fillMaxWidth(),
            navigationIcon = {
                IconButton(
                    onClick = { onBackPressedDispatcher.onBackPressed() }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = if (isSystemInDarkTheme()) DarkText else LightText
                    )
                }
            },
            backgroundColor = MaterialTheme.colors.primary
        )
    }

    private fun isEntriesValid(
        image: ByteArray?,
        name: String?,
        surname: String?,
        city: String?,
        email: String?,
        phoneNumber: String?,
        birthdate: LocalDate?,
    ): Boolean {
        return Validation(
            context = this,
            name = name,
            surname = surname,
            email = email,
            phoneNumber = phoneNumber
        ).run {
            isNameValid().also {
                nameError = !it
            } && isSurnameValid().also {
                surnameError = !it
            } && isEmailValid().also {
                emailError = !it
            } && isPhoneNumberValid().also {
                phoneNumberError = !it
            } && isAnyChangeAvailable(
                image = image,
                name = name,
                surname = surname,
                city = city,
                email = email,
                phoneNumber = phoneNumber,
                birthdate = birthdate,
            )
        }
    }

    private fun isAnyChangeAvailable(
        image: ByteArray?,
        name: String?,
        surname: String?,
        city: String?,
        email: String?,
        phoneNumber: String?,
        birthdate: LocalDate?,
    ): Boolean {
        if (
            image?.equals(contacts?.image) == true &&
            name?.equals(contacts?.name) == true &&
            surname?.equals(contacts?.surname) == true &&
            city?.equals(contacts?.city) == true &&
            email?.equals(contacts?.email) == true &&
            phoneNumber?.equals(contacts?.phoneNumber) == true &&
            birthdate?.equals(contacts?.birthdate) == true
        ) {
            return false
        }
        return true
    }
}