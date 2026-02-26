package ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

//create as composable to run numerous times, similar to OnPaint()
@Composable
//LoginScreen function; Unit returns nothing, just adding email/password strings
fun LoginScreen(
    onLoginClick: (String, String) -> Unit,
    onCreateAccountClick: () -> Unit
) {
    //create variables email/password, use remember to not reset compose
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    //functions for email, password, and login button usage
    fun onEmailChanged(newValue: String) {
        email = newValue
    }
    fun onPasswordChanged(newValue: String) {
        password = newValue
    }
    fun handleLoginButtonClick() {
        onLoginClick(email, password)
    }

    //set column; fill the screen (any size), add comfortable padding to not be tight
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //App name/logo placeholder
        Text(
            text = "Digital File Cabinet",
            //MaterialTheme will allow for auto adjust for dark/light themes
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 48.dp, bottom = 48.dp)
        )

        Text("Email")
        OutlinedTextField(
            value = email,
            onValueChange = ::onEmailChanged,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        Text("Password")
        OutlinedTextField(
            value = password,
            onValueChange = ::onPasswordChanged,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = ::handleLoginButtonClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log In")
        }

        Spacer(Modifier.height(16.dp))

        TextButton(onClick = onCreateAccountClick) {
            Text("Create Account")
        }
    }
}

