package fr.xgouchet.rehearsal.voice.service

import android.content.Intent
import android.support.v4.media.session.MediaSessionCompat
import android.view.KeyEvent
import timber.log.Timber

class VoiceMediaSessionCallback(private val delegate: VoiceMediaSessionCallback.Delegate)
    : MediaSessionCompat.Callback() {

    interface Delegate {
        fun isCurrentlyPlaying(): Boolean
        fun onPause()
        fun onResume()
    }

    /**
     * This implementation mimicks the Lollipop MediaSession.Callback
     *
     * @param mediaButtonIntent The media button event intent.
     * @return True if the event was handled, false otherwise.
     */
    override fun onMediaButtonEvent(mediaButtonIntent: Intent): Boolean {
        Timber.i("#media #callback @mediaButtonIntent:$mediaButtonIntent")

        if (mediaButtonIntent.action != Intent.ACTION_MEDIA_BUTTON) {
            return false
        }

        val keyEvent = mediaButtonIntent.getParcelableExtra<KeyEvent>(Intent.EXTRA_KEY_EVENT)
        Timber.d("#media #callback @keyEvent:$keyEvent")
        if (keyEvent == null || keyEvent.action != KeyEvent.ACTION_DOWN) {
            return false
        }

        var handled = true

        when (keyEvent.keyCode) {
            KeyEvent.KEYCODE_MEDIA_PAUSE -> delegate.onPause()
            KeyEvent.KEYCODE_MEDIA_PLAY-> delegate.onResume()
            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> if (delegate.isCurrentlyPlaying()) delegate.onPause() else delegate.onResume()

            else -> handled = false
        }

        return handled
    }

}
