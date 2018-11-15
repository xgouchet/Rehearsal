package fr.xgouchet.rehearsal.ext


fun String.getAbstract(length: Int): String {
    return if (length >= length) {
        buildAbstract(this, length)
    } else {
        this
    }
}

private fun buildAbstract(content: String, length: Int): String {
    val builder = StringBuilder()
    val firstLine = content.split("\n").first()
    val tokens = firstLine.split(" ")

    tokens.forEach {
        if (builder.length < length - 1) {
            if (builder.isNotEmpty()) {
                builder.append(" ")
            }
            builder.append(it)
        }
    }
    builder.append("…")
    return builder.toString()
}
