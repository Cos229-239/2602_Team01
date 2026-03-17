package ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import model.Node
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import kotlinproject.composeapp.generated.resources.*
import androidx.compose.foundation.combinedClickable


@Composable
fun CategoryTile(
    title: String,
    icon: DrawableResource,
    isContainer: Boolean,
    onClick: () -> Unit,
    onLongPress: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().aspectRatio(1f).combinedClickable(
            onClick = onClick,
            onLongClick = onLongPress
        ),
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
                    text = "Item",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}