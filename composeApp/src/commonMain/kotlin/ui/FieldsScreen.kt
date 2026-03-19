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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FieldsScreen(
    node: Node,
    onBack: () -> Unit,
    onAddField: () -> Unit,
    onAddPhoto: () -> Unit,
    onAddDocument: () -> Unit,
    onAddReminder: () -> Unit
) {

    var showAddMenu by remember { mutableStateOf(false) }

    fun dismissAddMenu() {
        showAddMenu = false
    }

    Box(modifier = Modifier.fillMaxSize()) {

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
                //Data Fields Section
                if(node.fields.isNotEmpty()) {
                    item { Text("Data Fields", style = MaterialTheme.typography.titleMedium) }
                    items(node.fields.size) {
                            i -> val field = node.fields[i]
                        DataFieldCard(field)
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

            onAddField = onAddField,
            onAddPhoto = onAddPhoto,
            onAddDocument = onAddDocument,
            onAddReminder = onAddReminder,

            onDismiss = ::dismissAddMenu,
        )

    }
}

@Composable
fun DataFieldCard(field: Field) {
    OutlinedCard( modifier = Modifier.fillMaxWidth().heightIn(min = 120.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)
        ) {
            Text(field.label)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = field.value,
                onValueChange = { field.value = it },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
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

