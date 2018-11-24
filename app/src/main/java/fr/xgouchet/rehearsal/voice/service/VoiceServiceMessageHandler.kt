package fr.xgouchet.rehearsal.voice.service

import android.os.Bundle
import android.os.Message
import android.os.Messenger
import fr.xgouchet.rehearsal.voice.SafeHandler
import fr.xgouchet.rehearsal.voice.ipc.MessageProtocol
import timber.log.Timber

class VoiceServiceMessageHandler(resource: VoiceService)
    : SafeHandler<VoiceService>(resource) {

    // region SafeHandler

    override fun handleMessage(msg: Message, resource: VoiceService): Boolean {
        return when (msg.what) {
            MessageProtocol.MSG_REGISTER_LISTENER -> handleRegisterListener(resource, msg.replyTo)
            MessageProtocol.MSG_PLAY_SCENE -> handlePlaySceneMessage(resource, msg.data)
            MessageProtocol.MSG_PAUSE_SCENE -> handlePauseSceneMessage(resource)
            else -> false
        }
    }

    // endregion

    // region Internal

    private fun handleRegisterListener(resource: VoiceService, replyTo: Messenger?): Boolean {
        return if (replyTo != null) {
            resource.registerListener(replyTo)
            true
        } else {
            false
        }
    }

    private fun handlePlaySceneMessage(resource: VoiceService, bundle: Bundle): Boolean {
        val sceneId = bundle.getLong(MessageProtocol.EXTRA_SCENE_ID)
        val cueId = bundle.getLong(MessageProtocol.EXTRA_CUE_ID)

        Timber.i("#voice #msg #play @sceneId:$sceneId @cueId:$cueId")
        resource.playSceneFromCue(sceneId, cueId)
        return true
    }

    private fun handlePauseSceneMessage(resource: VoiceService): Boolean {
        resource.pauseScene()
        return true
    }

    // endregion


}


