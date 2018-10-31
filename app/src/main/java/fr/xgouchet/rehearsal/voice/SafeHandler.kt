package fr.xgouchet.rehearsal.voice

import android.os.Handler
import android.os.Message
import java.lang.ref.WeakReference

open class SafeHandler<R>(resource: R)
    : Handler() {


    private val reference: WeakReference<R> = WeakReference(resource)

    /**
     * {@inheritDoc}
     */
    final override fun handleMessage(msg: Message) {
        val resource = reference.get()
        val handled: Boolean

        handled = if (resource == null) {
            handleMessageAlone(msg)
        } else {
            handleMessage(msg, resource)
        }

        if (!handled) {
            super.handleMessage(msg)
        }
    }

    /**
     * Called when a message is received and the token is still available
     *
     * @param msg       the received message
     * @param resource  the initial resource
     * @return true if the message has been handled
     */
    protected open fun handleMessage(msg: Message, resource: R): Boolean {
        return false
    }

    /**
     * This method is called to handle a message when the token
     * is not available anymore (it has been garbage collected)
     *
     * @param msg the received message
     * @return true if the message has been handled
     */
    protected open fun handleMessageAlone(Fmsg: Message): Boolean {
        return false
    }


}
