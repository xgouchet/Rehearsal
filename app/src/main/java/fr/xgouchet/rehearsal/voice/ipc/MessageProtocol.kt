package fr.xgouchet.rehearsal.voice.ipc

object MessageProtocol {

    const val MSG_REGISTER_LISTENER = 10

    const val MSG_PLAY_SCENE = 20
    const val MSG_PAUSE_SCENE = 21

    const val MSG_READING_CUE = 30
    const val MSG_STOPPED = 31

    const val EXTRA_SCENE_ID = "fr.xgouchet.rehearsal.extra.SCENE_ID"
    const val EXTRA_CUE_ID = "fr.xgouchet.rehearsal.extra.CUE_ID"

}
