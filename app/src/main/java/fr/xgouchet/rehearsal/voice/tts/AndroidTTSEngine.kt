package fr.xgouchet.rehearsal.voice.tts

import android.annotation.TargetApi
import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import timber.log.Timber
import java.util.Locale


class AndroidTTSEngine(
        preferredProvider: String?,
        context: Context
) : UtteranceProgressListener(),
        TTSEngine {


    private var textToSpeech: TextToSpeech? = null
    private var isInitialised: Boolean = false
    private var isAttached: Boolean = false
    private var listener: TTSEngine.Listener? = null

    init {
        Timber.i("#tts #created")
        val listener = TextToSpeech.OnInitListener {
            // On some Sony, the onInit is called on the same thread, meaning that the mTextToSpeech
            // field is null when we get there.
            // Which is why we need this double check mechanism to ensure we attach our listener
            isInitialised = true
            Timber.i("#tts #initialised")
            ensureListenerIsAttached()
        }
        textToSpeech = TextToSpeech(context, listener, preferredProvider)
        ensureListenerIsAttached()
    }

    // region UtteranceProgressListener

    override fun onStart(utteranceId: String) {
        fireOnStart(utteranceId)
    }

    override fun onDone(utteranceId: String) {
        fireOnDone(utteranceId)
    }

    override fun onError(utteranceId: String) {
        fireOnDone(utteranceId)
    }

    override fun onError(utteranceId: String, errorCode: Int) {
        fireOnDone(utteranceId)
    }

    // endregion

    // region TTSEngine

    override fun setListener(listener: TTSEngine.Listener) {
        this.listener = listener
    }

    override fun isReady(): Boolean {
        return isInitialised
    }

    override fun setPitch(pitch: Float) {
        textToSpeech?.setPitch(pitch)

    }

    override fun setRate(rate: Float) {
        textToSpeech?.setSpeechRate(rate)
    }

    override fun speak(message: String,
                       utteranceId: String,
                       locale: Locale) {
        if (!isInitialised) {
            Timber.w("#voice trying to speak but #engine not initialised")
            return
        }

        if (!isAttached) {
            Timber.w("#voice trying to speak but #listener not attached")
            ensureListenerIsAttached()
        }

        textToSpeech?.language = locale

        speakMessage(message, utteranceId, AudioManager.STREAM_MUSIC)
    }

    override fun stop() {
        if (!isInitialised) {
            Timber.w("#voice trying to speak but #engine not initialised")
            return
        }

        textToSpeech?.stop()
    }

    // endregion

    // region Internal

    @Synchronized private fun ensureListenerIsAttached() {
        if (textToSpeech != null && isInitialised && !isAttached) {
            textToSpeech?.setOnUtteranceProgressListener(this@AndroidTTSEngine)
            isAttached = true
            Timber.i("#tts #attached")
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun speakMessage(message: String,
                             utteranceId: String,
                             streamType: Int) {
        val params = Bundle()
        params.putInt(TextToSpeech.Engine.KEY_PARAM_STREAM, streamType)
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
        textToSpeech?.speak(message, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
    }

    private fun fireOnStart(utteranceId: String) {
        if (listener != null) {
            listener?.onStart(utteranceId)
        }
    }

    private fun fireOnDone(utteranceId: String) {
        if (listener != null) {
            listener?.onDone(utteranceId)
        }
    }
}

