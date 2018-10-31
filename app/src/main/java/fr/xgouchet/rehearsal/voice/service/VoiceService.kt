package fr.xgouchet.rehearsal.voice.service

import android.content.Intent
import android.os.Bundle
import android.os.HandlerThread
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import androidx.lifecycle.LifecycleService
import fr.xgouchet.rehearsal.voice.ipc.MessageProtocol
import fr.xgouchet.rehearsal.voice.tts.AndroidTTSEngine
import timber.log.Timber

class VoiceService
    : LifecycleService(),
        SceneReader.Listener {

    private lateinit var handlerThread: HandlerThread
    private val listeningMessengers: MutableList<Messenger> = mutableListOf()
    private lateinit var inMessenger: Messenger
    private lateinit var messageHandler: VoiceServiceMessageHandler

    private lateinit var voiceSceneReader: VoiceSceneReader

    // region Service

    override fun onCreate() {
        super.onCreate()

        handlerThread = HandlerThread("VoiceService")
        handlerThread.start()

        messageHandler = VoiceServiceMessageHandler(this)
        inMessenger = Messenger(messageHandler)

        voiceSceneReader = VoiceSceneReader(applicationContext, this, this) {
            AndroidTTSEngine(it, this@VoiceService)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        super.onBind(intent)
        return inMessenger.binder
    }

    override fun onDestroy() {
        super.onDestroy()
        messageHandler.removeCallbacksAndMessages(null)
        handlerThread.quit()
    }

    // endregion

    // region VoiceService

    fun registerListener(listener: Messenger) {
        listeningMessengers.add(listener)
    }

    fun playSceneFromCue(sceneId: Int, cueId: Int) {
        voiceSceneReader.playSceneFromCue(sceneId, cueId)
    }


    fun pauseScene() {
        voiceSceneReader.pauseScene()
    }

    // endregion

    // region SceneReader.Listener

    override fun readingCue(cueId: Int) {
        sendMessage {
            val message = Message.obtain(null, MessageProtocol.MSG_READING_CUE)

            val bundle = Bundle(1)
            bundle.putInt(MessageProtocol.EXTRA_CUE_ID, cueId)
            message.data = bundle

            message
        }
    }

    override fun stopped() {
        sendMessage { Message.obtain(null, MessageProtocol.MSG_STOPPED) }
    }

    // endregion

    // region Internal

    private fun sendMessage(messageBuilder: () -> Message) {
        listeningMessengers.forEach {
            val message = try {
                messageBuilder()
            } catch (e: Exception) {
                Timber.e(e, "#error #building #ipc #message")
                null
            }

            try {
                if (message != null) {
                    it.send(message)
                }
            } catch (e: RemoteException) {
                Timber.e(e, "#error #sending #ipc @message:$message")
            } catch (e: IllegalStateException) {
                Timber.e(e, "#error #sending #ipc @message:$message")
            }
        }
    }

    // endregion
}
