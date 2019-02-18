package fr.xgouchet.rehearsal.voice.ipc

import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import timber.log.Timber
import java.util.Queue

sealed class MessageStrategy {
    abstract fun send(message: Message)
}


class Unconnected(private val outMessageQueue: Queue<Message>) : MessageStrategy() {

    override fun send(message: Message) {
        outMessageQueue.add(message)
        Timber.e("#queued #ipc @message:$message")
    }
}

class Connected(private val outMessenger: Messenger,
                private val inMessenger: Messenger) : MessageStrategy() {

    override fun send(message: Message) {
        try {
            message.replyTo = inMessenger
            outMessenger.send(message)
            Timber.e("#sent #ipc @message:$message")
        } catch (e: RemoteException) {
            Timber.e(e, "#error #sending #ipc @message:$message")
        } catch (e: IllegalStateException) {
            Timber.e(e, "#error #sending #ipc @message:$message")
        } catch (e : IllegalStateException){
            Timber.e(e, "#error #sending #ipc @message:$message")
        }
    }
}

