package fr.xgouchet.rehearsal.voice.app

interface VoiceServiceListener {
    fun readingCue(cueId: Int)
    fun stopped()
}
