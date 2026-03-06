package ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    onAdd: () -> Unit,
    onSettings: () -> Unit
) {
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
            FloatingActionButton(onClick = onAdd) {
                Icon(
                    painter = painterResource(Res.drawable.ic_add_circle),
                    contentDescription = "Add",
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    ) { paddingValues ->
        if (node.children.isNotEmpty()) {
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
                        onClick = { onNodeClick(child) }
                    )
                }
            }
        } else {
            //leaf node -> show fields later
            Box(
                modifier = Modifier.padding(paddingValues).fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("Fields Screen to come")
            }
        }
    }
}