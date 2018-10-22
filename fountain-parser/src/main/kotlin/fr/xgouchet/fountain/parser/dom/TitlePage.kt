package fr.xgouchet.fountain.parser.dom

data class TitlePage(
        val title: String,
        val metadata: Map<String, String>
) {

    data class Builder(
            var title: String = "",
            val metadata: MutableMap<String, String> = mutableMapOf()
    ) {
        fun addMetadata(key: String, value: String) {
            metadata[key] = value
        }

        fun build(): TitlePage {
            return TitlePage(
                    title = title,
                    metadata = metadata
            )
        }
    }
}
