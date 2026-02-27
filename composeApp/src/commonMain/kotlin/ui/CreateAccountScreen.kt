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

    //error handling variables
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showError by remember { mutableStateOf(false) }

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
    //validate input
    fun validateInput(): Boolean {
        if(email.isBlank()) {
            errorMessage = "Email cannot be empty"
            showError = true
            return false
        }
        if(password.isBlank()) {
            errorMessage = "Password cannot be empty"
            showError = true
            return false
        }
        if(password != confirmPassword) {
            errorMessage = "Passwords do not match"
            showError = true
            return false
        }
        //clear error if good validation
        showError = false
        errorMessage = null
        return true
    }
    fun handleCreateAccountClick() {
        if(validateInput()) {
            onCreateAccountClick(email, password)
        }
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

        Spacer(Modifier.height(16.dp))

        Text("Password")
        OutlinedTextField(
            value = password,
            onValueChange = ::onPasswordChanged,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(16.dp))

        Text("Confirm Password")
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = ::onConfirmPasswordChanged,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        //Display errors
        if (showError && errorMessage != null) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error
            )
        }

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