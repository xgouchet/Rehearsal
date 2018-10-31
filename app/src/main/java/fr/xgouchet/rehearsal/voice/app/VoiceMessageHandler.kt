package fr.xgouchet.rehearsal.voice.app

import android.os.Bundle
import android.os.Message
import fr.xgouchet.rehearsal.voice.SafeHandler
import fr.xgouchet.rehearsal.voice.ipc.MessageProtocol

class VoiceMessageHandler(resource: VoiceServiceListener)
    : SafeHandler<VoiceServiceListener>(resource) {

    // region SafeHandler

    override fun handleMessage(msg: Message, resource: VoiceServiceListener): Boolean {
        return when (msg.what) {
            MessageProtocol.MSG_READING_CUE -> handleReadingCue(resource, msg.data)
            MessageProtocol.MSG_STOPPED -> handleStoppedMessage(resource)
            else -> false
        }
    }

    // endregion

    private fun handleReadingCue(resource: VoiceServiceListener,
                                 data: Bundle?)
            : Boolean {
        val cueId = data?.getInt(MessageProtocol.EXTRA_CUE_ID)

        return if (cueId != null) {
            resource.readingCue(cueId)
            true
        } else {
            false
        }
    }

    private fun handleStoppedMessage(resource: VoiceServiceListener): Boolean {
        resource.stopped()
        return true
    }
}
