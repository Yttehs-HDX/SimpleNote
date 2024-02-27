package org.eviyttehsarch.note.extra

fun String.limitContent(maxLength: Int, ellipsis: String = "..."): String {
    val trimmedContent = replace("\n", " ")
    return if (trimmedContent.length <= maxLength) {
        trimmedContent
    } else {
        trimmedContent.take(maxLength) + ellipsis
    }
}