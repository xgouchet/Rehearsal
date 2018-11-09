package fr.xgouchet.rehearsal.voice.service

import fr.xgouchet.rehearsal.core.room.join.CueWithCharacter

interface SceneReader {

    fun playSceneFromCue(sceneId: Int, cueId: Int)

    fun pauseScene()

    fun resume()

    interface Listener {

        fun readingCue(cue: CueWithCharacter)

        fun stopped()
    }
}
