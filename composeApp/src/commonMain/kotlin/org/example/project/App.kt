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
import data.Template
import data.instantiateTemplate
import model.Field

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

        fun reorderNodes(parent: Node, fromIndex: Int, toIndex: Int) {
            val list = parent.children.toMutableList()
            val item = list.removeAt(fromIndex)
            list.add(toIndex, item)

            val updated = parent.copy(children = list)

            rootNode = updateNodeInTree(rootNode, updated)
            navigationStack =
                navigationStack.mapNotNull { findNodeById(rootNode, it.id) }
        }

        fun reorderFields(fromIndex: Int, toIndex: Int) {
            //make a mutable copy of current fields
            val fieldsList = currentNode.fields.toMutableList()
            //remove field at fromIndex
            val field = fieldsList.removeAt(fromIndex)
            //add field at toIndex
            fieldsList.add(toIndex, field)
            //create updated Node
            val updatedNode = currentNode.copy(fields = fieldsList)
            //update rootNode and rebuild navigation stack
            rootNode = updateNodeInTree(rootNode, updatedNode)
            navigationStack =
                navigationStack.mapNotNull { findNodeById(rootNode, it.id) }
        }

        fun deleteField(nodeId: String, fieldId: String) {
            fun update(current: Node): Node {
                if (current.id == nodeId) {
                    return current.copy(
                        fields = current.fields.filter { it.id != fieldId }.toMutableList()
                    )
                }
                return current.copy(
                    children = current.children.map { update(it) }
                )
            }
            rootNode = update(rootNode)
            navigationStack = navigationStack.mapNotNull { findNodeById(rootNode, it.id) }
        }

        fun isDescendant(parent: Node, targetId: String): Boolean {
            if (parent.id == targetId) return true
            return parent.children.any { isDescendant(it, targetId)}
        }

        fun moveNode(node: Node, newParentId: String) {
            if (isDescendant(node, newParentId)) return

            fun removeNode(current: Node): Node {
                val newChildren = current.children
                    .filter { it.id != node.id }
                    .map { removeNode(it) }
                return current.copy(children = newChildren)
            }
            val treeWithoutNode = removeNode(rootNode)

            val destination = findNodeById(treeWithoutNode, newParentId) ?: return

            val updatedParent = destination.copy(
                children = destination.children + node
            )

            rootNode = updateNodeInTree(treeWithoutNode, updatedParent)
            navigationStack =
                navigationStack.mapNotNull { findNodeById(rootNode, it.id) }
        }

        fun updateField(nodeId: String, fieldId: String, newValue: String) {

            fun update(current: Node): Node {
                if (current.id == nodeId) {
                    val updatedFields = current.fields.map {
                        if (it.id == fieldId) it.copy(value = newValue)
                        else it
                    }.toMutableList()

                    return current.copy(fields = updatedFields)
                }

                return current.copy(
                    children = current.children.map { update(it) }
                )
            }

            rootNode = update(rootNode)
            navigationStack =
                navigationStack.mapNotNull { findNodeById(rootNode, it.id) }
        }

        fun renameField(nodeId: String, fieldId: String, newLabel: String) {
            val fieldsList = currentNode.fields.map { field ->
                if (field.id == fieldId) field.copy(label = newLabel)
                else field
            }.toMutableList()
            val updatedNode = currentNode.copy(fields = fieldsList)
            rootNode = updateNodeInTree(rootNode, updatedNode)
            navigationStack = navigationStack.mapNotNull { findNodeById(rootNode, it.id) }
        }

        fun addField(name: String) {
            if (name.isBlank()) return

            val newField = Field(
                id = IdGenerator.newId(),
                label = name,
                value = ""
            )

            val updatedNode = currentNode.copy(
                fields = (currentNode.fields + newField).toMutableList()
            )

            rootNode = updateNodeInTree(rootNode, updatedNode)
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

        fun addNodeToParent(parentId: String, newNode: Node) {
            fun recurse(node: Node): Node {
                if (node.id == parentId) {
                    return node.copy(
                        children = node.children + newNode
                    )
                }
                return node.copy(
                    children = node.children.map { recurse(it) }
                )
            }
            rootNode = recurse(rootNode)
            navigationStack =
                navigationStack.mapNotNull { findNodeById(rootNode, it.id) }
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

        //adding templates
        fun addTemplate(parentId: String, template: Template) {
            val newNode = instantiateTemplate(template)
            addNodeToParent(parentId, newNode)
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
                        rootNode = rootNode,
                        onNodeClick = ::navigateTo,
                        onBack = ::navigateBack,
                        onAddFolder = { addNode(true) },
                        onAddItem = { addNode(false) },
                        onAddTemplate = { template ->
                            addTemplate(currentNode.id, template)
                        },
                        onSettings = ::onSettings,
                        onRenameNode = ::renameNode,
                        onDeleteNode = ::deleteNode,
                        onMoveNode = ::moveNode,
                        onReorder = ::reorderNodes
                    )
                } else {
                    FieldsScreen(
                        node = currentNode,
                        onBack = ::navigateBack,
                        onAddField = { fieldName -> addField(fieldName) },
                        onAddPhoto = { /* TODO */ },
                        onAddDocument = { /* TODO */ },
                        onAddReminder = { /* TODO */ },
                        onUpdateField = ::updateField,
                        onReorderField = ::reorderFields,
                        onDeleteField = ::deleteField,
                        onRenameField = ::renameField
                    )
                }
            }
        }
        //name dialog
        if(showNameDialog) {
            AlertDialog(
                onDismissRequest = { showNameDialog = false },
                title = { Text(if (pendingAddFolder) "New Folder Name" else "New File Name") },
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

