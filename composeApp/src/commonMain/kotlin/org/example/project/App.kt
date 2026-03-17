package org.example.project

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ui.LoginScreen
import ui.CreateAccountScreen
import model.Node
import data.SampleTree
import ui.AddMenuSheet
import ui.NodeScreen
import ui.FieldsScreen
import data.IdGenerator

@Composable
@Preview
fun App() {
    MaterialTheme {

        // Tree and Node navigation
        var rootNode by remember { mutableStateOf(SampleTree.root) }
        var navigationStack by remember { mutableStateOf(listOf(rootNode)) }
        val currentNode = navigationStack.last()

        fun navigateTo(node: Node) {
            navigationStack = navigationStack + node
        }

        fun navigateBack() {
            if(navigationStack.size > 1) {
                navigationStack = navigationStack.dropLast(1)
            }
        }

        fun updateNodeInTree(current: Node, updated: Node): Node {
            if (current.id == updated.id) {
                return updated
            }
            val newChildren = current.children.map {
                updateNodeInTree(it, updated)
            }
            return current.copy(children = newChildren)
        }

        fun findNodeById(current: Node, id: String): Node? {
            if(current.id == id) {
                return current
            }
            for (child in current.children) {
                val result = findNodeById(child, id)
                if(result != null) return result
            }
            return null
        }
        //helper to always update rootNode
        fun addNodeToCurrent(newNode: Node) {
            //add new node to current node's children
            val updateNode = currentNode.copy(children = currentNode.children + newNode)
            //update rootNode recursively
            rootNode = updateNodeInTree(rootNode, updateNode)
            //rebuild navstack from updated rootNode
            navigationStack = navigationStack.mapNotNull { findNodeById(rootNode, it.id) }
        }

        //functions for Icon rename/delete/move
        fun renameNode(target: Node, newName: String) {
            val updatedNode = target.copy(title = newName)

            rootNode = updateNodeInTree(rootNode, updatedNode)

            navigationStack = navigationStack.mapNotNull {
                findNodeById(rootNode, it.id)
            }
        }

        fun deleteNode(target: Node) {
            fun removeNode(current: Node): Node {
                val newChildren = current.children
                    .filter { it.id != target.id }
                    .map { removeNode(it) }
                return current.copy(children = newChildren)
            }
            rootNode = removeNode(rootNode)

            navigationStack = navigationStack.mapNotNull {
                findNodeById(rootNode, it.id)
            }
        }

        fun moveNodeUp(target: Node) {
            if(navigationStack.size < 2) return

            val parent = navigationStack[navigationStack.size - 2]

            deleteNode(target)

            val updatedParent =
                parent.copy(children = parent.children + target)

            rootNode = updateNodeInTree(rootNode, updatedParent)

            navigationStack =
                navigationStack.mapNotNull { findNodeById(rootNode, it.id) }
        }

        //screen state
        var currentScreen by remember { mutableStateOf(AppScreen.LOGIN) }

        //add folder/item dialog state
        var showNameDialog by remember { mutableStateOf(false) }
        var newNodeName by remember { mutableStateOf("") }
        var pendingAddFolder by remember { mutableStateOf(false) } //true-folder, false-item

        fun addNode(isFolder: Boolean) {
            pendingAddFolder = isFolder
            newNodeName = ""
            showNameDialog = true
        }

        fun confirmAddNode() {
            if(newNodeName.isBlank()) return
            val newNode = Node(
                id = IdGenerator.newId(),
                title = newNodeName,
                icon = currentNode.icon,
                isContainer = pendingAddFolder
            )
            addNodeToCurrent(newNode)

            showNameDialog = false
        }

        //helper functions
        /*fun nodeHasData(node: Node): Boolean {
            return node.fields.isNotEmpty() ||
                    node.documents.isNotEmpty() ||
                    node.pictures.isNotEmpty()
        }

        fun promoteNode(node: Node): Node {
            val promotedChild = Node(
                id = IdGenerator.newId(),
                title = node.title,
                icon = node.icon,
                isContainer = false,
                fields = node.fields,
                documents = node.documents,
                pictures = node.pictures
            )

            return node.copy(
                fields = mutableListOf(),
                documents = mutableListOf(),
                pictures = mutableListOf(),
                children = listOf(promotedChild),
                isContainer = true
            )
        }*/
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
                if (currentNode.isContainer) {
                    NodeScreen(
                        node = currentNode,
                        onNodeClick = ::navigateTo,
                        onBack = ::navigateBack,
                        onAddFolder = { addNode(true) },
                        onAddItem = { addNode(false) },
                        onSettings = ::onSettings,
                        onRenameNode = ::renameNode,
                        onDeleteNode = ::deleteNode,
                        onMoveNode = ::moveNodeUp
                    )
                } else {
                    FieldsScreen(
                        node = currentNode,
                        onBack = ::navigateBack,
                        onAddField = { /* TODO */ },
                        onAddPhoto = { /* TODO */ },
                        onAddDocument = { /* TODO */ },
                        onAddReminder = { /* TODO */ }
                    )
                }
            }
        }
        //name dialog
        if(showNameDialog) {
            AlertDialog(
                onDismissRequest = { showNameDialog = false },
                title = { Text(if (pendingAddFolder) "New Folder Name" else "New Item Name") },
                text = {
                    OutlinedTextField(
                        value = newNodeName,
                        onValueChange = { newNodeName = it },
                        placeholder = { Text("Enter Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    Button(onClick = ::confirmAddNode) { Text("Create") }
                },
                dismissButton = {
                    Button(onClick = {
                        showNameDialog = false
                    }) { Text("Cancel") }
                }
            )
        }
    }
}

