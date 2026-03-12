package ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMenuSheet(
    visible: Boolean,
    modifier: Modifier = Modifier,
    isFolderContext: Boolean = false, //true-add folder/item, false-fieldscreen
    onAddFolder: () -> Unit,
    onAddItem: () -> Unit,
    onAddField: () -> Unit,
    onAddPhoto: () -> Unit,
    onAddDocument: () -> Unit,
    onAddReminder: () -> Unit,
    onDismiss: () -> Unit
) {
    if (!visible) return //only show when visible

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            //always allowed
            if(isFolderContext) {
                Button(
                    onClick = onAddFolder,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Folder")
                }
                Button(
                    onClick = onAddItem,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Item")
                }
            } else {
                Button(
                    onClick = onAddField,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Text Field")
                }

                Button(
                    onClick = onAddPhoto,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Photo")
                }

                Button(
                    onClick = onAddDocument,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Document")
                }

                Button(
                    onClick = onAddReminder,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Reminder")
                }
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}