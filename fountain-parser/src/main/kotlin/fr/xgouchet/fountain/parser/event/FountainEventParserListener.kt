package fr.xgouchet.fountain.parser.event

interface FountainEventParserListener {

    fun lineRead(lineEvent: LineEvent)

    fun scriptEnd()

}
