package ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.DrawableResource
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.ic_add_circle
import kotlinproject.composeapp.generated.resources.ic_attach_money
import kotlinproject.composeapp.generated.resources.ic_corporate_fare
import kotlinproject.composeapp.generated.resources.ic_directions_car
import kotlinproject.composeapp.generated.resources.ic_home
import kotlinproject.composeapp.generated.resources.ic_settings


data class HomeCategory(
    val label: String,
    val icon: DrawableResource
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSettingsClick: () -> Unit,
    onAddClick: () -> Unit,
    onCategoryClick: (String) -> Unit
) {
    //temp placeholder categories
    val categories = remember { listOf (
        HomeCategory("Home", Res.drawable.ic_home),
        HomeCategory("Vehicle", Res.drawable.ic_directions_car),
        HomeCategory("Financial", Res.drawable.ic_attach_money),
        HomeCategory("Business", Res.drawable.ic_corporate_fare)
    )
    }

    Scaffold(
        topBar = {
           CenterAlignedTopAppBar(
                title = {
                    //Logo placeholder
                    Text("Digital File Cabinet")
                },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_settings),
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(
                    painter = painterResource(Res.drawable.ic_add_circle),
                    contentDescription = "Add Category"
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
            items(categories) { category ->
                CategoryTile(
                    category = category,
                    onClick = onCategoryClick
                )
            }
        }
    }
}

@Composable
fun CategoryTile(
    category: HomeCategory,
    onClick: (String) -> Unit
) {
    fun handleClick() {
        onClick(category.label)
    }

    Card(
        modifier = Modifier.fillMaxWidth().aspectRatio(1f),
        onClick = ::handleClick
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(category.icon),
                contentDescription = category.label,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = category.label,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}


