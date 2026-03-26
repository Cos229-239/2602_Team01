package ui

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier

@Composable
fun ItemMenu(
    expanded: Boolean,
    itemName: String,
    onDismiss: () -> Unit,
    onRename: ((String) -> Unit)? = null,
    onDelete: (() -> Unit)? = null,
    onMove: (() -> Unit)? = null,
    onReorder: (() -> Unit)? = null
) {
    var showRenameDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showFinalDelete by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(itemName) }

    //Menu
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        if (onRename != null) {
            DropdownMenuItem(
                text = { Text("Rename") },
                onClick = {
                    text = itemName
                    showRenameDialog = true
                    onDismiss()
                }
            )
        }
        if (onDelete != null) {
            DropdownMenuItem(
                text = { Text("Delete") },
                onClick = {
                    showDeleteDialog = true
                    onDismiss()
                }
            )
        }
        if (onMove != null) {
            DropdownMenuItem(
                text = { Text("Move to...") },
                onClick = {
                    onMove()
                    onDismiss()
                }
            )
        }
        if (onReorder != null) {
            DropdownMenuItem(
                text = { Text("Reorder on this page") },
                onClick = {
                    onReorder()
                    onDismiss()
                }
            )
        }
    }
    //rename
    if (showRenameDialog && onRename != null) {
        AlertDialog(
            onDismissRequest = { showRenameDialog = false },
            title = { Text("Rename") },
            text = {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(onClick = {
                    onRename(text)
                    showRenameDialog = false
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(onClick = { showRenameDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    //delete step 1
    if (showDeleteDialog && onDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete") },
            text = { Text("Are you sure you want to delete this item?") },
            confirmButton = {
                Button(onClick = {
                    showDeleteDialog = false
                    showFinalDelete = true
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    //delete step 2
    if (showFinalDelete && onDelete != null) {
        AlertDialog(
            onDismissRequest = { showFinalDelete = false },
            title = { Text("Confirm Delete") },
            text = { Text("This action cannot be undone.") },
            confirmButton = {
                Button(onClick = {
                    onDelete()
                    showFinalDelete = false
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(onClick = { showFinalDelete = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}