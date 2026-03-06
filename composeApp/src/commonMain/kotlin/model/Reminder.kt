package model

data class Reminder(
    val id: String,
    val fieldId: String,
    val triggerTime: Long,
    val message: String
)