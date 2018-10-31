package fr.xgouchet.rehearsal.voice.service

interface SceneReader {

    fun playSceneFromCue(sceneId: Int, cueId: Int)

    fun pauseScene()

    interface Listener {

        fun readingCue(cueId: Int)

        fun stopped()
    }
}
