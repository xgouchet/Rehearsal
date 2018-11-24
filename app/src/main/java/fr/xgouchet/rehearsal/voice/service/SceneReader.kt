package fr.xgouchet.rehearsal.voice.service

import fr.xgouchet.rehearsal.core.model.Cue

interface SceneReader {

    fun playSceneFromCue(sceneId: Long, cueId: Long)

    fun pauseScene()

    fun resume()

    interface Listener {

        fun readingCue(cue: Cue)

        fun stopped()
    }
}
