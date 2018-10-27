package fr.xgouchet.fountain.parser.event

sealed class LineEvent

object BlankLineEvent : LineEvent()

data class RawLineEvent(val content: String) : LineEvent()

data class MetadataEvent(
        val key: String,
        val value: String
) : LineEvent()

data class SceneHeaderEvent(
        val type: String,
        val description: String,
        val numbering: String = ""
) : LineEvent()

data class TransitionEvent(
        val description: String
) : LineEvent()

data class CharacterCueEvent(
        val name: String,
        val extension: String = "",
        val dual: Boolean = false
) : LineEvent()

data class DialogEvent(
        val content: String
) : LineEvent()

data class ParentheticalEvent(
        val content: String
) : LineEvent()


data class ActionEvent(
        val content: String
) : LineEvent()

object PageBreakEvent : LineEvent()

data class SectionEvent(
        val title : String,
        val level : Int
) : LineEvent()

data class SynopsisEvent(
        val content : String
) : LineEvent()

data class LyricsEvent(
        val content : String
) : LineEvent()

data class CharacterLyricsEvent(
        val content : String
) : LineEvent()
