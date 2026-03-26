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

    var reorderMode by remember { mutableStateOf(false) }
    var reorderFromIndex by remember { mutableStateOf<Int?>(null) }

    var nodeToMove by remember { mutableStateOf<Node?>(null) }

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
            if(reorderMode && reorderFromIndex != null) {
                Text(
                    text = "Select destination for '${selectedNode?.title}'",
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
                        node = child,
                        title = child.title,
                        icon = child.icon,
                        isContainer = child.isContainer,
                        onClick = {
                            //if in reorder mode, use click as destination
                            if (reorderMode && reorderFromIndex != null && reorderFromIndex!! >= 0) {
                                onReorder(node, reorderFromIndex!!, index)
                                reorderFromIndex = null
                                reorderMode = false
                                selectedNode = null
                            }
                            //if moving, use click as destination folder
                            else if (nodeToMove != null) {
                                onMoveNode(nodeToMove!!, child.id)
                                nodeToMove = null
                                selectedNode = null
                            }
                            else {
                                onNodeClick(child)
                            }
                        },
                        onRenameNode = onRenameNode,
                        onDeleteNode = onDeleteNode,
                        onMove = {
                            nodeToMove = child
                        },
                        onReorderStart = {
                            reorderFromIndex = index
                            reorderMode = true
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

    if (nodeToMove != null) {
        AlertDialog(
            onDismissRequest = { nodeToMove = null },
            title = { Text("Select destination") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    FolderPicker(rootNode) { selected ->
                        onMoveNode(nodeToMove!!, selected.id)
                        nodeToMove = null
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                Button(onClick = { nodeToMove = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}