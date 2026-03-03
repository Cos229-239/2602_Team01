package model

import org.jetbrains.compose.resources.DrawableResource

data class Node(
    val id: String,
    val title: String,
    val icon: DrawableResource,
    val children: List<Node> = emptyList(),
    val fields: List<Field> = emptyList()
)

data class Field(
    val id: String,
    val label: String,
    val value: String
)