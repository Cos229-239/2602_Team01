package data

import kotlin.random.Random

object IdGenerator {

    fun newId(): String {
        val chars = "abcdefghijklmnopqrstuvwxyz0123456789"
        return (1..16)
            .map { chars[Random.nextInt(chars.length)] }
            .joinToString("")
    }
}