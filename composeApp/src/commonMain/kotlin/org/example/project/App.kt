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
import model.Node
import data.SampleTree
import ui.NodeScreen

@Composable
@Preview
fun App() {
    MaterialTheme {

        //Node navigation
        var navigationStack by remember { mutableStateOf(listOf(SampleTree.root)) }
        val currentNode = navigationStack.last()

        fun navigateTo(node: Node) {
            navigationStack = navigationStack + node
        }

        fun navigateBack() {
            if(navigationStack.size > 1) {
                navigationStack = navigationStack.dropLast(1)
            }
        }

        //screen state
        var currentScreen by remember { mutableStateOf(AppScreen.LOGIN) }

        //screen change functions
        fun onLoginSuccess(email: String, password: String) {
            //later validate with Supabase
            currentScreen = AppScreen.HOME
        }
        fun onGoToCreateAccount() {
            currentScreen = AppScreen.CREATE_ACCOUNT
        }
        fun onCreateAccount(email: String, password: String) {
            //add Supabase later
            currentScreen = AppScreen.HOME
        }
        fun onBackToLogin() {
            currentScreen = AppScreen.LOGIN
        }

        //local functions
        fun onSettings() {
            //will navigate to settings screen
        }
        fun onAddCategory() {
            //will show add and edit options
        }

        //switching screens
        when (currentScreen) {
            AppScreen.LOGIN -> {
                LoginScreen(
                    onLoginClick = ::onLoginSuccess,
                    onCreateAccountClick = ::onGoToCreateAccount
                )
            }
            AppScreen.CREATE_ACCOUNT -> {
                CreateAccountScreen(
                    onCreateAccountClick = ::onCreateAccount,
                    onBackToLoginClick = ::onBackToLogin
                )
            }
            AppScreen.HOME -> {
                NodeScreen(
                    node = currentNode,
                    onNodeClick = ::navigateTo,
                    onBack = ::navigateBack,
                    onAdd = ::onAddCategory,
                    onSettings = ::onSettings
                )
            }
        }
    }
}

