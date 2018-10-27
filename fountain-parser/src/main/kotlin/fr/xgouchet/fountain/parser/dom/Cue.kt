package fr.xgouchet.fountain.parser.dom

sealed class Cue {
    interface Builder {
        fun build(): Cue
    }
}

data class CharacterCue(
        val characterName: String,
        val characterExtension: String?,
        val parts: List<CharacterCue.Part>
) : Cue() {

    abstract class Part

    data class Dialog(val line: String) : Part()

    data class Parenthetical(val direction: String) : Part()


    data class Builder(
            var characterName: String = "",
            var characterExtension: String? = null,
            val parts: MutableList<CharacterCue.Part> = mutableListOf()
    ) : Cue.Builder {

        fun addDialog(line: String) {
            parts.add(Dialog(line))
        }

        fun addParenthetical(direction: String) {
            parts.add(Parenthetical(direction))
        }

        override fun build(): Cue {
            return CharacterCue(
                    characterName = characterName,
                    characterExtension = characterExtension,
                    parts = parts
            )
        }
    }

}


data class ActionCue(
        val direction: String
) : Cue() {

    data class Builder(
            var direction: String = ""
    ) : Cue.Builder {

        override fun build(): Cue {
            return ActionCue(direction = direction)
        }
    }
}

data class LyricsCue(
        val lyrics: String
) : Cue() {

    data class Builder(
            var lyrics: String = ""
    ) : Cue.Builder {
        override fun build(): Cue {
            return LyricsCue(lyrics)
        }

    }
}

object PageBreakCue : Cue() {

    object Builder : Cue.Builder {
        override fun build(): Cue = PageBreakCue
    }
}
