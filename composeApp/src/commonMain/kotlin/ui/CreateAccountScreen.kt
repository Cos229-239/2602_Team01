package ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun CreateAccountScreen(
    onCreateAccountClick: (String, String) -> Unit,
    onBackToLoginClick: () -> Unit
) {
    //variables
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    //helper functions
    fun onEmailChanged(newValue: String) {
        email = newValue
    }
    fun onPasswordChanged(newValue: String) {
        password = newValue
    }
    fun onConfirmPasswordChanged(newValue: String) {
        confirmPassword = newValue
    }
    fun handleCreateAccountClick() {
        //will add a confirmation that password = confirmPassword later
        onCreateAccountClick(email, password)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Create Account",
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
            onClick = ::handleCreateAccountClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Account")
        }

        Spacer(Modifier.height(16.dp))

        TextButton(onClick = onBackToLoginClick) {
            Text("Return to Login")
        }
    }
}