package fr.xgouchet.fountain.parser.dom

data class TitlePage(
        val metadata: Map<String, String>
) {

    val title: String
        get() {
            return metadata.entries
                    .asSequence()
                    .filter { it.key.equals(KEY_TITLE, ignoreCase = true) }
                    .map { it.value }
                    .firstOrNull().orEmpty()
        }

    val author: String
        get() {
            return metadata.entries
                    .asSequence()
                    .filter { it.key.equals(KEY_AUTHOR, ignoreCase = true) || it.key.equals(KEY_AUTHORS, ignoreCase = true) }
                    .map { it.value }
                    .joinToString(separator = " / ")
        }

    data class Builder(
            val metadata: MutableMap<String, String> = mutableMapOf()
    ) {
        fun addMetadata(key: String, value: String) {
            metadata[key] = value
        }

        fun build(): TitlePage {
            return TitlePage(
                    metadata = metadata
            )
        }
    }

    companion object {
        private const val KEY_TITLE = "title"
        private const val KEY_AUTHOR = "author"
        private const val KEY_AUTHORS = "authors"
    }
}
