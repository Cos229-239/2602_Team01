package org.example.project

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ui.LoginScreen
import ui.CreateAccountScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        var isLoggedIn by remember { mutableStateOf(false) }

        fun onLoginSuccess(email: String, password: String) {
            isLoggedIn = true
        }

        fun onCreateAccount() {
            //add later
        }

        if(isLoggedIn) {
            HomeScreen()
        } else {
            LoginScreen(
                onLoginClick = ::onLoginSuccess,
                onCreateAccountClick = ::onCreateAccount
            )
        }
    }
}

@Composable
fun HomeScreen () {
    Text(text = "Home Screen Placeholder",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(top = 48.dp, bottom = 48.dp)
    )
}
