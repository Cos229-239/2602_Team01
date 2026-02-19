package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource

import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.compose_multiplatform
import ui.LoginScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        var isLoggedIn by remember { mutableStateOf(false) }

        if(isLoggedIn) {
            Text(text = "Home Screen Placeholder",
                //MaterialTheme will allow for auto adjust for dark/light themes
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 48.dp, bottom = 48.dp))
        } else {
            LoginScreen(
                onLoginClick = { email, password -> isLoggedIn = true
                },
                onCreateAccountClick = {
                    //will navigate to sign up screen
                }
            )
        }
    }
}