package fr.xgouchet.fountain.parser.dom

import fr.xgouchet.fountain.parser.event.ActionEvent
import fr.xgouchet.fountain.parser.event.CharacterCueEvent
import fr.xgouchet.fountain.parser.event.DialogEvent
import fr.xgouchet.fountain.parser.event.FountainEventParser
import fr.xgouchet.fountain.parser.event.FountainEventParserListener
import fr.xgouchet.fountain.parser.event.LineEvent
import fr.xgouchet.fountain.parser.event.LyricsEvent
import fr.xgouchet.fountain.parser.event.MetadataEvent
import fr.xgouchet.fountain.parser.event.PageBreakEvent
import fr.xgouchet.fountain.parser.event.ParentheticalEvent
import fr.xgouchet.fountain.parser.event.SceneHeaderEvent
import fr.xgouchet.fountain.parser.event.SectionEvent
import fr.xgouchet.fountain.parser.event.SynopsisEvent
import fr.xgouchet.fountain.parser.event.TransitionEvent
import java.io.InputStream

class FountainDomParser {

    fun parse(inputStream: InputStream): Script {
        val parser = FountainEventParser()
        val listener = FountainDomEventListener()
        parser.parse(inputStream, listener)

        return listener.buildScript()
    }

    internal class FountainDomEventListener
        : FountainEventParserListener {

        private val scriptBuilder = Script.Builder()
        private var currentScene: Scene.Builder? = null
        private var currentCharacter: CharacterCue.Builder? = null

        // region FountainEventParserListener

        override fun lineRead(lineEvent: LineEvent) {

            when (lineEvent) {
                is MetadataEvent -> addMetadata(lineEvent)
                is SceneHeaderEvent -> startScene(lineEvent)
                is CharacterCueEvent -> startCharacterCue(lineEvent)
                is DialogEvent -> addDialog(lineEvent)
                is ParentheticalEvent -> addParenthetical(lineEvent)
                is ActionEvent -> addAction(lineEvent)
                is PageBreakEvent -> addPageBreak()
                is TransitionEvent -> addTransition(lineEvent)
                is LyricsEvent -> addLyrics(lineEvent)
                is SectionEvent,
                is SynopsisEvent -> {
                    // Ignored for now…
                }
                else -> {
                    System.err.println("unhandled event : $lineEvent")
                }
            }
        }

        override fun scriptEnd() {}

        // endregion

        // region FountainDomEventListener

        internal fun buildScript(): Script {
            return scriptBuilder.build()
        }

        // endregion

        // region Listener.Internal

        private fun addMetadata(event: MetadataEvent) {
            scriptBuilder.titlePage.addMetadata(event.key, event.value)
        }

        private fun startScene(event: SceneHeaderEvent) {
            val builder = Scene.Builder()

            builder.header.apply {
                type = event.type
                description = event.description
                numbering = event.numbering
            }

            scriptBuilder.addPart(builder)
            currentScene = builder
            currentCharacter = null
        }


        private fun addTransition(event: TransitionEvent) {
            currentScene = null

            scriptBuilder.addPart(Transition.Builder(event.description))
        }


        private fun startCharacterCue(event: CharacterCueEvent) {
            val builder = CharacterCue.Builder()

            builder.characterName = event.name
            builder.characterExtension = if (event.extension.isBlank()) null else event.extension

            currentScene?.addCue(builder)
            currentCharacter = builder
        }

        private fun addDialog(event: DialogEvent) {
            currentCharacter?.addDialog(event.content)
        }

        private fun addParenthetical(event: ParentheticalEvent) {
            currentCharacter?.addParenthetical(event.content)
        }

        private fun addAction(event: ActionEvent) {
            currentScene?.addCue(ActionCue.Builder(event.content))
        }

        private fun addPageBreak() {
            currentScene?.addCue(PageBreakCue.Builder)
        }

        private fun addLyrics(event: LyricsEvent) {
            currentScene?.addCue(LyricsCue.Builder(event.content))
        }

        // endregion


    }
}
