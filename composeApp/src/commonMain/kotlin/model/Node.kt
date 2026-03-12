package model

import org.jetbrains.compose.resources.DrawableResource

data class Node(
    val id: String,
    val title: String,
    val icon: DrawableResource,
    val isContainer: Boolean = true,
    val children: List<Node> = emptyList(),

    val fields: MutableList<Field> = mutableListOf(),
    val documents: MutableList<DocumentReference> = mutableListOf(),
    val pictures: MutableList<ImageReference> = mutableListOf()
)
