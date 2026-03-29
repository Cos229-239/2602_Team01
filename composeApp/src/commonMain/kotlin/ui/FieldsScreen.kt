package ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import model.Node
import model.ImageReference
import model.DocumentReference
import org.jetbrains.compose.resources.painterResource
import kotlinproject.composeapp.generated.resources.*
import model.Field
import ui.AddMenuSheet
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FieldsScreen(
    node: Node,
    onBack: () -> Unit,
    onAddField: (String) -> Unit,
    onAddPhoto: () -> Unit,
    onAddDocument: () -> Unit,
    onAddReminder: () -> Unit,
    onUpdateField: (String, String, String) -> Unit,
    onReorderField: (Int, Int) -> Unit,
    onDeleteField: (String, String) -> Unit,
    onRenameField: (nodeId: String, fieldId: String, newLabel: String) -> Unit
) {

    var showAddMenu by remember { mutableStateOf(false) }
    var showFieldDialog by remember { mutableStateOf(false) }
    var newFieldName by remember { mutableStateOf("") }

    //var selectedField by remember { mutableStateOf<Field?>(null) }
    //var showFieldMenu by remember { mutableStateOf(false) }

    var reorderMode by remember { mutableStateOf(false) }
    var reorderFromIndex by remember { mutableStateOf<Int?>(null) }

    val focusManager = LocalFocusManager.current

    fun dismissAddMenu() {
        showAddMenu = false
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .clickable(
                onClick = { focusManager.clearFocus() },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(node.title + " Data") },
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
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { showAddMenu = true },
                    modifier = Modifier.padding(bottom = 16.dp, end = 16.dp)) {
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
                if(reorderMode && reorderFromIndex != null) {
                    item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                        Text(
                            text = "Select destination field",
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
                //Data Fields Section
                if(node.fields.isNotEmpty()) {
                    item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                        Text("Data Fields", style = MaterialTheme.typography.titleMedium)
                    }
                    itemsIndexed(
                        node.fields,
                        span = { _, _ -> GridItemSpan(maxCurrentLineSpan) }
                    ) { index, field ->

                        DataFieldCard(

                            field = field,
                            nodeId = node.id,
                            onUpdateField = onUpdateField,
                            onDeleteField = onDeleteField,
                            onReorderStart = {
                                reorderFromIndex = index
                                reorderMode = true
                            },
                            onRenameField = onRenameField,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    if (reorderMode && reorderFromIndex != null && reorderFromIndex!! >= 0) {
                                        onReorderField(reorderFromIndex!!, index)
                                        reorderFromIndex = null
                                        reorderMode = false
                                    }
                                }
                        )
                    }
                }
                //Documents Section
                if(node.documents.isNotEmpty()) {
                    item { Text("Documents", style = MaterialTheme.typography.titleMedium) }
                    items(node.documents.size) {
                            i -> val doc = node.documents[i]
                        DocumentCard(doc)
                    }
                }
                //Pictures Section
                if(node.pictures.isNotEmpty()) {
                    item { Text("Photos", style = MaterialTheme.typography.titleMedium) }
                    items(node.pictures.size) {
                            i -> val pic = node.pictures[i]
                        PhotoCard(pic)
                    }
                }
            }
        }

        //overlay add menu
        AddMenuSheet(
            visible = showAddMenu,
            isFolderContext = false,

            onAddFolder = {},
            onAddFile = {},
            onAddTemplate = {},

            onAddField = {
                newFieldName = ""
                showFieldDialog = true
                showAddMenu = false
            },
            onAddPhoto = onAddPhoto,
            onAddDocument = onAddDocument,
            onAddReminder = onAddReminder,

            onDismiss = ::dismissAddMenu,
        )
    }
    if (showFieldDialog) {
        AlertDialog(
            onDismissRequest = { showFieldDialog = false },
            title = { Text("New Data Field Name") },
            text = {
                OutlinedTextField(
                    value = newFieldName,
                    onValueChange = { newFieldName = it },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onAddField(newFieldName)
                        showFieldDialog = false
                    }
                ) { Text("Create") }
            },
            dismissButton = {
                Button(
                    onClick = { showFieldDialog = false }
                ) { Text("Cancel") }
            }
        )
    }

}

@Composable
fun DataFieldCard(field: Field,
                  nodeId: String,
                  onUpdateField: (String, String, String) -> Unit,
                  onRenameField: (nodeId: String, fieldId: String, newLabel: String) -> Unit,
                  onDeleteField: (String, String) -> Unit,
                  onReorderStart: () -> Unit,
                  //onMenuClick: (() -> Unit)? = null,
                  modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedCard(modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)
            ) {
                Text(field.label)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = field.value,
                    onValueChange = { onUpdateField(nodeId, field.id, it) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        }
        //Ellipsis + anchored menu
        Box(modifier = Modifier.align(Alignment.TopEnd)) {

            IconButton(onClick = { showMenu = true }) {
                Icon(
                    painter = painterResource(Res.drawable.ic_more_vert),
                    contentDescription = "Menu",
                    modifier = Modifier.size(30.dp)
                )
            }

            ItemMenu(
                expanded = showMenu,
                itemName = field.label,
                onDismiss = { showMenu = false },
                onRename = { newLabel ->
                    onRenameField(nodeId, field.id, newLabel)
                    showMenu = false
                },
                onDelete = {
                    onDeleteField(nodeId, field.id)
                    showMenu = false
                },
                onReorder = {
                    onReorderStart()
                    showMenu = false
                }
            )
        }
    }
}

@Composable
fun PhotoCard(photo: ImageReference) {
    OutlinedCard(modifier = Modifier.fillMaxWidth().heightIn(min = 120.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)
        ) {
            Text("Photo")
            Spacer(modifier = Modifier.height(8.dp))
            Text(photo.path.substringAfterLast("/"))
        }
    }
}

@Composable
fun DocumentCard(document: DocumentReference) {
    OutlinedCard(modifier = Modifier.fillMaxWidth().heightIn(min = 120.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)
        ) {
            Text("Document")
            Spacer(modifier = Modifier.height(8.dp))
            Text(document.name)
        }
    }
}


