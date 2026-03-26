package ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import model.Node
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import kotlinproject.composeapp.generated.resources.*
import androidx.compose.foundation.combinedClickable
import androidx.compose.ui.text.style.LineHeightStyle

@Composable
fun CategoryTile(
    node: Node,
    title: String,
    icon: DrawableResource,
    isContainer: Boolean,
    onClick: () -> Unit,
    onRenameNode: (Node, String) -> Unit,
    onDeleteNode: (Node) -> Unit,
    onMove: (Node) -> Unit,
    onReorderStart: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Box {
        Box(modifier = Modifier.fillMaxWidth()) {
            //main card content
            Card(
                modifier = Modifier.fillMaxWidth().aspectRatio(1f).clickable {
                    onClick() },
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(
                            if (isContainer) {
                                Res.drawable.ic_folder
                            } else {
                                Res.drawable.ic_docs
                            }
                        ),
                        contentDescription = title,
                        modifier = Modifier.size(if (isContainer) 56.dp else 36.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    if(!isContainer) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "File",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        Box(modifier = Modifier.align(Alignment.TopEnd)) {
            IconButton(onClick = { showMenu = true}) {
                Icon(
                    painter = painterResource(Res.drawable.ic_more_vert),
                    contentDescription = "Menu",
                    modifier = Modifier.size(36.dp)
                )
            }
            ItemMenu(
                expanded = showMenu,
                itemName = title,
                onDismiss = { showMenu = false },
                onRename = { newName ->
                    onRenameNode(node, newName)
                    showMenu = false
                },
                onDelete = {
                    onDeleteNode(node)
                    showMenu = false
                },
                onMove = {
                    onMove(node)
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