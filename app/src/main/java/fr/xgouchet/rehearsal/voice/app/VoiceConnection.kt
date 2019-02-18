package fr.xgouchet.rehearsal.voice.app

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import fr.xgouchet.rehearsal.voice.ipc.Connected
import fr.xgouchet.rehearsal.voice.ipc.MessageProtocol
import fr.xgouchet.rehearsal.voice.ipc.MessageStrategy
import fr.xgouchet.rehearsal.voice.ipc.Unconnected
import fr.xgouchet.rehearsal.voice.service.VoiceService
import timber.log.Timber
import java.util.concurrent.ConcurrentLinkedQueue

class VoiceConnection(listener: VoiceServiceListener)
    : ServiceConnection {

    private var outMessenger: Messenger? = null

    private val outMessageQueue = ConcurrentLinkedQueue<Message>()
    private var messageStrategy: MessageStrategy = Unconnected(outMessageQueue)

    private val inMessenger = Messenger(VoiceMessageHandler(listener))

    // region VoiceConnection

    fun bind(context: Context) {

        Timber.i("#voice #connecting to #service")

        val intent = Intent(context, VoiceService::class.java)
        context.startService(intent)
        context.bindService(intent, this, Service.BIND_AUTO_CREATE)

        // send initial messages
        sendMessage(Message.obtain(null, MessageProtocol.MSG_REGISTER_LISTENER))
    }

    fun unbind(context: Context) {
        Timber.i("#voice #disconnecting from #service")
        messageStrategy = Unconnected(outMessageQueue)
        context.unbindService(this)
    }

    fun sendMessage(message: Message) {
        messageStrategy.send(message)
    }

    // endregion

    // region ServiceConnection

    override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
        Timber.i("#voice #connected to #service @name:$name @binder:$binder")

        outMessenger = Messenger(binder)

        messageStrategy = Connected(Messenger(binder), inMessenger)

        outMessageQueue.forEach {
            val newMessage = Message.obtain(it)
            messageStrategy.send(newMessage)
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        Timber.i("#voice #disconnected from #service @name:$name")
        messageStrategy = Unconnected(outMessageQueue)
        outMessenger = null
    }

    // endregion
}
