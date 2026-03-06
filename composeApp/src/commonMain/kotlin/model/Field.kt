package model

data class Field(
    val id: String,
    val label: String,

    var value: String = "",

    val type: FieldType = FieldType.TEXT,

    val fromTemplate: Boolean = false,
    val position: Int = 0,
    val reminderId: String? = null
)

enum class FieldType {
    TEXT,
    YEAR,
    NUMBER,
    DATE,
    BOOLEAN,
    CUSTOM
}