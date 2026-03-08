package ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import ui.AddMenuSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FieldsScreen(
    node: Node,
    onBack: () -> Unit,
    onAdd: () -> Unit
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

            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues)
                    .padding(16.dp).verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.Start
            ) {
                //Documents Section
                if (node.documents.isNotEmpty()) {
                    Text("Documents", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    for (i in 0 until node.documents.size) {
                        val doc = node.documents[i]
                        Text(doc.name)
                    }
                    Spacer(Modifier.height(16.dp))
                }

                //Pictures Section
                if (node.pictures.isNotEmpty()) {
                    Text("Pictures", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    for (i in 0 until node.pictures.size) {
                        val pic = node.pictures[i]
                        Text(pic.path.substringAfterLast("/")) //will replace with image display later
                    }
                    Spacer(Modifier.height(16.dp))
                }

                //Text Fields Section
                Text("Data Fields", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                for (i in 0 until node.fields.size) {
                    val field = node.fields[i]
                    OutlinedTextField(
                        value = field.value,
                        onValueChange = { newValue -> field.value = newValue },
                        label = { Text(field.label) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }
        }

        //overlay add menu
        AddMenuSheet(
            visible = showAddMenu,
            onAddField = ::dismissAddMenu,
            onAddPhoto = ::dismissAddMenu,
            onAddDocument = ::dismissAddMenu,
            onAddReminder = ::dismissAddMenu,
            onDismiss = ::dismissAddMenu,
        )
    }
}

