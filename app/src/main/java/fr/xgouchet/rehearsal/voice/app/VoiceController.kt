package fr.xgouchet.rehearsal.voice.app

import android.content.Context
import android.os.Bundle
import android.os.Message
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import fr.xgouchet.rehearsal.voice.ipc.MessageProtocol

class VoiceController(context: Context)
    : LifecycleObserver,
        VoiceServiceListener {

    public var listener: VoiceServiceListener? = null

    private val appContext: Context = context.applicationContext
    private val connection = VoiceConnection(this)
    private val mBinderLock = Any()

    // region LifecycleObserver

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun connectListener() {
        synchronized(mBinderLock) {
            connection.bind(appContext)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun disconnectListener() {
        synchronized(mBinderLock) {
            connection.unbind(appContext)
        }
    }

    // endregion

    // region VoiceServiceListener

    override fun readingCue(cueId: Int) {
        listener?.readingCue(cueId)
    }

    override fun stopped() {
        listener?.stopped()
    }
    // endregion

    // region VoiceController

    fun playFromCue(sceneId: Int, cueId: Int) {
        val message = Message.obtain(null, MessageProtocol.MSG_PLAY_SCENE)

        val bundle = Bundle(2)
        bundle.putInt(MessageProtocol.EXTRA_SCENE_ID, sceneId)
        bundle.putInt(MessageProtocol.EXTRA_CUE_ID, cueId)
        message.data = bundle

        connection.sendMessage(message)
    }

    fun stop() {
        val message = Message.obtain(null, MessageProtocol.MSG_PAUSE_SCENE)
        connection.sendMessage(message)
    }

    // endregion
}
