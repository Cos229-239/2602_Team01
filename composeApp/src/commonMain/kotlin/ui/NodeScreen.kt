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
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NodeScreen(
    node: Node,
    rootNode: Node,
    onNodeClick: (Node) -> Unit,
    onBack: () -> Unit,
    onAddFolder: () -> Unit,
    onAddItem: () -> Unit,
    onSettings: () -> Unit,
    onRenameNode: (Node, String) -> Unit,
    onDeleteNode: (Node) -> Unit,
    onMoveNode: (Node, String) -> Unit,
    onReorder: (Node, Int, Int) -> Unit
) {
    var showAddMenu by remember { mutableStateOf(false) }

    //menu for long press/right click of icons
    var selectedNode by remember { mutableStateOf<Node?>(null) }
    var showNodeMenu by remember { mutableStateOf(false) }
    var showRenameDialog by remember { mutableStateOf(false) }
    var renameText by remember { mutableStateOf("") }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showFinalDeleteDialog by remember { mutableStateOf(false) }

    var reorderMode by remember { mutableStateOf(false) }
    var draggedIndex by remember { mutableStateOf<Int?>(null) }
    var nodeToMove by remember { mutableStateOf<Node?>(null) }
    var showMoveDialog by remember { mutableStateOf(false) }

    fun dismissAddMenu() { showAddMenu = false }

    @Composable
    fun FolderPicker(
        node: Node,
        level: Int = 0,
        onSelect: (Node) -> Unit
    ) {
        Column {
            if (node.isContainer) {
                TextButton(
                    onClick = { onSelect(node) },
                    modifier = Modifier.padding(start = (level * 16).dp)
                ) {
                    Text(node.title)
                }
            }
            node.children.forEach {
                FolderPicker(it, level + 1, onSelect)
            }
        }
    }

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

        Column(
            modifier = Modifier.padding(paddingValues).fillMaxSize()
        ) {
            if(reorderMode) {
                Text(
                    text = "Select Destination",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(8.dp)
                )
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(16.dp).weight(1f),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(node.children) { index, child ->
                    CategoryTile(
                        title = child.title,
                        icon = child.icon,
                        isContainer = child.isContainer,
                        onClick = {
                            if (reorderMode && draggedIndex != null) {
                                onReorder(node, draggedIndex!!, index)
                                draggedIndex = null
                                reorderMode = false
                            } else {
                                onNodeClick(child)
                            }
                        },
                        onLongPress = {
                            selectedNode = child
                            showNodeMenu = true
                        },
                        onDoubleTap = {
                            reorderMode = true
                            draggedIndex = index
                        }
                    )
                }
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
        onAddFile = {
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
                            showDeleteConfirmDialog = true
                            showNodeMenu = false
                        }
                    ) { Text("Delete") }

                    TextButton(
                        onClick = {
                            nodeToMove = selectedNode
                            showMoveDialog = true
                            showNodeMenu = false
                        }
                    ) { Text("Move") }
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
    if(showDeleteConfirmDialog && selectedNode != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete '${selectedNode!!.title}'?") },
            confirmButton = {
                Button(onClick = {
                    showDeleteConfirmDialog = false
                    showFinalDeleteDialog = true
                }) { Text("Yes") }
            },
            dismissButton = {
                Button(onClick = { showDeleteConfirmDialog = false})
                { Text("Cancel") }
            }
        )
    }
    if(showFinalDeleteDialog && selectedNode != null) {
        AlertDialog(
            onDismissRequest = { showFinalDeleteDialog = false },
            title = { Text("Final Confirmation") },
            text = { Text("Deleting '${selectedNode!!.title}' cannot be undone. " +
                    "Press Confirm to delete.") },
            confirmButton = {
                Button(onClick = {
                    onDeleteNode(selectedNode!!)
                    showFinalDeleteDialog = false
                    selectedNode = null
                }) { Text("Confirm") }
            },
            dismissButton = {
                Button(onClick = { showFinalDeleteDialog = false })
                { Text("Cancel") }
            }
        )
    }
    if(showMoveDialog && nodeToMove != null) {
        AlertDialog(
            onDismissRequest = { showMoveDialog = false },
            title = { Text("Move to...") },
            text = {
                Column(
                    modifier = Modifier.heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    FolderPicker(rootNode) { destination ->
                        onMoveNode(nodeToMove!!, destination.id)
                        showMoveDialog = false
                    }
                }
            },
            confirmButton = {}
        )
    }
}