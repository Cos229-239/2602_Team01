package data

import model.*
import data.IdGenerator

object NodeRepository {

    fun addField(node: Node, label: String, type: FieldType = FieldType.TEXT) {
        node.fields.add(
            Field(
                id = IdGenerator.newId(),
                label = label,
                value = "",
                type = type
            )
        )
    }

    fun updateField(field: Field, newValue: String) {
        field.value = newValue
    }

    fun removeField(node: Node, fieldId: String) {
        node.fields.removeAll { it.id == fieldId }
    }

    fun addDocument(node: Node, name: String, path: String) {
        node.documents.add(
            DocumentReference(
                id = IdGenerator.newId(),
                name = name,
                path = path
            )
        )
    }

    fun addImage(node: Node, path: String) {
        node.pictures.add(
            ImageReference(
                id = IdGenerator.newId(),
                path = path
            )
        )
    }

    fun removeDocument(node: Node, id: String) {
        node.documents.removeAll { it.id == id }
    }

    fun removeImage(node: Node, id: String) {
        node.pictures.removeAll { it.id == id }
    }
}