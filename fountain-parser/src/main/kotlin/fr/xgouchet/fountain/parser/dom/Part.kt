package fr.xgouchet.fountain.parser.dom


sealed class Part {
    interface Builder {
        fun build(): Part
    }
}


data class Scene(
        val header: Scene.Header,
        val cues: List<Cue>
) : Part() {

    data class Builder(
            var header: Scene.Header.Builder = Scene.Header.Builder(),
            val cues: MutableList<Cue.Builder> = mutableListOf()
    ) : Part.Builder {

        fun addCue(cue: Cue.Builder) {
            cues.add(cue)
        }

        override fun build(): Part {
            return Scene(
                    header = header.build(),
                    cues = cues.map { it.build() }
            )
        }
    }


    data class Header(
            val type: String,
            val description: String,
            val numbering: String
    ) {

        data class Builder(
                var type: String = "",
                var description: String = "",
                var numbering: String = ""
        ) {

            fun build(): Header {
                return Header(
                        type = type,
                        description = description,
                        numbering = numbering
                )
            }
        }
    }
}

data class Transition(
        val description: String
) : Part() {

    data class Builder(
            var description: String
    ) : Part.Builder {
        override fun build(): Part {
            return Transition(description)
        }

    }
}
