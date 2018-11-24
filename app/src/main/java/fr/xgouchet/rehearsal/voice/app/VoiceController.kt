package fr.xgouchet.rehearsal.voice.app

import android.content.Context
import android.os.Bundle
import android.os.Message
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import fr.xgouchet.rehearsal.voice.ipc.MessageProtocol
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VoiceController(context: Context)
    : LifecycleObserver,
        VoiceServiceListener {

    var listener: VoiceServiceListener? = null

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
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                delay(500)
            }
            synchronized(mBinderLock) {
                connection.unbind(appContext)
            }
        }

    }

    // endregion

    // region VoiceServiceListener

    override fun readingCue(cueId: Long) {
        listener?.readingCue(cueId)
    }

    override fun stopped() {
        listener?.stopped()
    }
    // endregion

    // region VoiceController

    fun playFromCue(sceneId: Long, cueId: Long) {
        val message = Message.obtain(null, MessageProtocol.MSG_PLAY_SCENE)

        val bundle = Bundle(2)
        bundle.putLong(MessageProtocol.EXTRA_SCENE_ID, sceneId)
        bundle.putLong(MessageProtocol.EXTRA_CUE_ID, cueId)
        message.data = bundle

        connection.sendMessage(message)
    }

    fun stop() {
        val message = Message.obtain(null, MessageProtocol.MSG_PAUSE_SCENE)
        connection.sendMessage(message)
    }

    // endregion
}
