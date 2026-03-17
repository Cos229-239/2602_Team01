package ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle.Alignment
import androidx.compose.ui.unit.dp
import model.Node
import org.jetbrains.compose.resources.painterResource
import kotlinproject.composeapp.generated.resources.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NodeScreen(
    node: Node,
    onNodeClick: (Node) -> Unit,
    onBack: () -> Unit,
    onAddFolder: () -> Unit,
    onAddItem: () -> Unit,
    onSettings: () -> Unit,
    onRenameNode: (Node, String) -> Unit,
    onDeleteNode: (Node) -> Unit,
    onMoveNode: (Node) -> Unit
) {
    var showAddMenu by remember { mutableStateOf(false) }

    //menu for long press/right click of icons
    var selectedNode by remember { mutableStateOf<Node?>(null) }
    var showNodeMenu by remember { mutableStateOf(false) }
    var showRenameDialog by remember { mutableStateOf(false) }
    var renameText by remember { mutableStateOf("") }

    fun dismissAddMenu() { showAddMenu = false }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(node.title) },
                navigationIcon = {
                    if (node.id != "root") {
                        IconButton(onClick = onBack) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_arrow_back),
                                contentDescription = "Back",
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onSettings) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_settings),
                            contentDescription = "Settings",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddMenu = true }) {
                Icon(
                    painter = painterResource(Res.drawable.ic_add_circle),
                    contentDescription = "Add",
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    ) { paddingValues ->
         LazyVerticalGrid(
             columns = GridCells.Fixed(2),
             modifier = Modifier.padding(paddingValues).padding(16.dp),
             horizontalArrangement = Arrangement.spacedBy(16.dp),
             verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
             items(node.children) { child ->
                 CategoryTile(
                     title = child.title,
                     icon = child.icon,
                     isContainer = child.isContainer,
                     onClick = { onNodeClick(child) },
                     onLongPress = {
                         selectedNode = child
                         showNodeMenu = true
                     }
                 )
             }
         }
    }
    AddMenuSheet(
        visible = showAddMenu,
        isFolderContext = true, //show folder/item only
        onAddFolder = {
            onAddFolder()
            dismissAddMenu()
        },
        onAddItem = {
            onAddItem()
            dismissAddMenu()
        },
        onAddField = {},
        onAddPhoto = {},
        onAddDocument = {},
        onAddReminder = {},
        onDismiss = ::dismissAddMenu
    )

    //menu dialog for long press/right click
    if(showNodeMenu && selectedNode != null) {
        AlertDialog(
            onDismissRequest = { showNodeMenu = false },
            title = { Text(selectedNode!!.title) },
            text = {
                Column {

                    TextButton(
                        onClick = {
                            renameText = selectedNode!!.title
                            showRenameDialog = true
                            showNodeMenu = false
                        }
                    ) { Text("Rename") }

                    TextButton(
                        onClick = {
                            onDeleteNode(selectedNode!!)
                            showNodeMenu = false
                        }
                    ) { Text("Delete") }

                    TextButton(
                        onClick = {
                            onMoveNode(selectedNode!!)
                            showNodeMenu = false
                        }
                    ) { Text("Move Up a Folder") }
                }
            },
            confirmButton = {}
        )
    }
    if(showRenameDialog && selectedNode != null) {
        AlertDialog(
            onDismissRequest = { showRenameDialog = false },
            title = { Text("Rename ${selectedNode!!.title}") },
            text = {
                OutlinedTextField(
                    value = renameText,
                    onValueChange = { renameText = it },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onRenameNode(selectedNode!!, renameText)
                        showRenameDialog = false
                    }
                ) { Text("Save") }
            },
            dismissButton = {
                Button(
                    onClick = { showRenameDialog = false }
                ) { Text("Cancel") }
            }
        )
    }
}