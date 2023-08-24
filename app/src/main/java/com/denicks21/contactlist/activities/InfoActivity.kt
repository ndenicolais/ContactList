package com.denicks21.contactlist.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.denicks21.contactlist.R
import com.denicks21.contactlist.ui.theme.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class InfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            rememberSystemUiController().isStatusBarVisible = false
            ContactListTheme {
                val systemUiController = rememberSystemUiController()
                val barColor = MaterialTheme.colors.onSurface
                val barIcons = isSystemInDarkTheme()
                SideEffect {
                    systemUiController.setNavigationBarColor(
                        color = barColor,
                        darkIcons = barIcons
                    )
                }
                InfoPage()
            }
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun InfoPage() {
        val uriHandler = LocalUriHandler.current

        Scaffold(
            topBar = {
                TopBar()
            },
            content = {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.surface
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .wrapContentHeight()
                                .height(450.dp)
                                .width(450.dp),
                            shape = RoundedCornerShape(size = 8.dp),
                            backgroundColor = MaterialTheme.colors.background
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Card(
                                    modifier = Modifier
                                        .height(400.dp)
                                        .width(450.dp)
                                        .padding(start = 15.dp, end = 15.dp, top = 15.dp),
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
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        )
    }

    @Composable
    fun TopBar() {
        TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            title = {
                Text(
                    text = "Info Page",
                    color = if (isSystemInDarkTheme()) DarkText else LightText
                )
            },
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
}