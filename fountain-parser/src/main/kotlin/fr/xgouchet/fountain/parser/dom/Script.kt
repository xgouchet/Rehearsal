package fr.xgouchet.fountain.parser.dom


data class Script(
        val titlePage: TitlePage,
        val parts: List<Part>
) {

    data class Builder(
            var titlePage: TitlePage.Builder = TitlePage.Builder(),
            val parts: MutableList<Part.Builder> = mutableListOf()
    ) {
        fun build(): Script {
            return Script(
                    titlePage = titlePage.build(),
                    parts = parts.map { it.build() }
            )
        }

        fun addPart(part: Part.Builder) {
            parts.add(part)
        }

    }
}
