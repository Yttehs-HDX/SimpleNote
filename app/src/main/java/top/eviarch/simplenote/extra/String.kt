package top.eviarch.simplenote.extra

fun String.limitContent(maxLength: Int, ellipsis: String = "..."): String {
    val trimmedContent = replace("\n", " ")
    return if (trimmedContent.length <= maxLength) {
        trimmedContent
    } else {
        trimmedContent.take(maxLength) + ellipsis
    }
}

fun String.isJson(): Boolean {
    val jsonPattern = "^\\s*\\{.*\\}\\s*$|^\\s*\\[.*]\\s*$".toRegex()
    return jsonPattern.matches(this)
}