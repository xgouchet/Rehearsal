package fr.xgouchet.fountain.parser.event

import java.io.InputStream

class FountainEventParser {

    fun parse(inputStream: InputStream, listener: FountainEventParserListener) {
        inputStream
                .bufferedReader(Charsets.UTF_8)
                .useLines { processLines(it, listener) }
    }

    private fun processLines(lines: Sequence<String>, listener: FountainEventParserListener) {
        var previousLine: LineEvent = BlankLineEvent

        lines.forEach {
            val line = processLine(it, previousLine, listener)
            // println("@line:$line")
            previousLine = line ?: BlankLineEvent
        }

        // process a final blank line, to be sure
        processLine("", previousLine, listener)

        listener.scriptEnd()
    }

    private fun processLine(currentLine: String,
                            previousLine: LineEvent,
                            listener: FountainEventParserListener)
            : LineEvent? {
        var result: LineEvent? = null

        val powerUserBlock = parsePowerUser(currentLine)
        if (currentLine.isBlankLine()) {
            sendLineEvent(previousLine, listener)
            result = BlankLineEvent
        } else if (previousLine is BlankLineEvent) {
            val block = powerUserBlock ?: parseNewBlock(currentLine)
            result = if (block is RawLineEvent) ActionEvent(block.content) else block
        } else {
            val block = powerUserBlock ?: parseNewBlock(currentLine)

            when (previousLine) {
                is CharacterCueEvent,
                is ParentheticalEvent -> {
                    sendLineEvent(previousLine, listener)
                    result = if (block is ParentheticalEvent) block else DialogEvent(currentLine.trim())
                }

                is DialogEvent -> {
                    if (block is ParentheticalEvent) {
                        sendLineEvent(previousLine, listener)
                        result = block
                    } else {
                        result = DialogEvent(previousLine.content + "\n" + currentLine.trim())
                    }
                }

                is MetadataEvent -> {
                    if (block is MetadataEvent) {
                        sendLineEvent(previousLine, listener)
                        result = block
                    } else if (currentLine.startsWith("   ") || currentLine.startsWith("\t")) {
                        val previous = if (previousLine.value.isBlank()) "" else previousLine.value.trim() + "\n"
                        result = MetadataEvent(
                                key = previousLine.key,
                                value = previous + currentLine.trim()
                        )
                    }
                }
                is ActionEvent -> {
                    result = ActionEvent(previousLine.content + "\n" + currentLine)
                }
            }
        }

        return result
    }

    private fun sendLineEvent(previousLine: LineEvent, listener: FountainEventParserListener) {
        if (previousLine !is BlankLineEvent) {
            listener.lineRead(previousLine)
        }
    }

    private fun parsePowerUser(line: String): LineEvent? {
        val matches = powerUserBlockTransform
                .mapNotNull { transformMatching(line, it.key, it.value) }

        return matches.firstOrNull()
    }

    private fun parseNewBlock(line: String): LineEvent {
        val matches = newBlockTransform
                .mapNotNull { transformMatching(line, it.key, it.value) }

        return matches.firstOrNull() ?: RawLineEvent(line)
    }

    private fun transformMatching(line: String,
                                  regex: Regex,
                                  transform: (MatchResult) -> LineEvent): LineEvent? {
        val match = regex.matchEntire(line)
        return if (match != null) transform(match) else null
    }


    private fun String?.isBlankLine(): Boolean {
        return this == null || this == "" || this == " "
    }

    companion object {


        private val transitionPURegex = Regex("^>\\s*([^<>]*)\\s*$")
        private val sceneHeaderPURegex = Regex("^\\.\\s*([^#]*)(#(.*)#)?\\s*$")
        private val characterCuePURegex = Regex("^@\\s*(.*)\\s*(\\((.*)\\))?\\s*$")
        private val actionPURegex = Regex("^!\\s*(.*)\\s*$")

        private val powerUserBlockTransform = mapOf(
                transitionPURegex to { match: MatchResult ->
                    TransitionEvent(match.groupValues[1].trim())
                },
                sceneHeaderPURegex to { match: MatchResult ->
                    SceneHeaderEvent(
                            type = ".",
                            description = match.groupValues[1].trim(),
                            numbering = match.groupValues[3].trim()
                    )
                },
                characterCuePURegex to { match: MatchResult ->
                    CharacterCueEvent(
                            name = match.groupValues[1].trim(),
                            extension = match.groupValues[3].trim()
                    )
                },
                actionPURegex to { match: MatchResult ->
                    ActionEvent(match.groupValues[1].trim())
                }
        )


        private val transitionRegex = Regex("^\\s*(([^a-z]*TO:)|FADE TO BLACK\\.|FADE OUT\\.|CUT TO BLACK\\.)\\s*$")
        private val metadataRegex = Regex("^([\\w\\s]+):(.*)$")
        private val sceneHeaderRegex = Regex("^\\s*(INT/EXT |INT\\./EXT\\.|INT |EXT |EST |I/E |INT\\.|EXT\\.|EST\\.)([^#]*)(#(.*)#)?\\s*$")
        private val characterCueRegex = Regex("^\\s*([A-Z0-9_\\- ]+)\\s*(\\((.*)\\))?\\s*$")
        private val parenthetical = Regex("^\\s*\\((.*)\\)\\s*$")
        private val pageBreaks = Regex("^(===|---)$")

        private val newBlockTransform = mapOf(
                pageBreaks to { _: MatchResult -> PageBreakEvent },
                transitionRegex to { match: MatchResult ->
                    TransitionEvent(match.value.trim())
                },
                metadataRegex to { match: MatchResult ->
                    MetadataEvent(
                            key = match.groupValues[1].trim(),
                            value = match.groupValues[2].trim()
                    )
                },
                sceneHeaderRegex to { match: MatchResult ->
                    SceneHeaderEvent(
                            type = match.groupValues[1].trim(),
                            description = match.groupValues[2].trim(),
                            numbering = match.groupValues[4].trim()
                    )
                },
                characterCueRegex to { match: MatchResult ->
                    CharacterCueEvent(
                            name = match.groupValues[1].trim(),
                            extension = match.groupValues[3].trim()
                    )
                },
                parenthetical to { match: MatchResult ->
                    ParentheticalEvent(match.groupValues[1].trim())
                }
        )
    }
}


/**
TODO Emphasis
 *italics*
 **bold**
 ***bold italics***
_underline_



 **/
