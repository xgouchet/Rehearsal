package fr.xgouchet.rehearsal.voice.tts


/**
 * Describes a TTS Provider, ie: an object capable of "speaking" text messages
 */
interface TTSEngine {


    /**
     * A listener on TTS events
    k   */
    interface Listener {

        /**
         * Called when, or before a message is spoken by the TTS provider
         *
         * @param utteranceId the id provided in the [TTSEngine.speak] method
         */
        fun onStart(utteranceId: String)

        /**
         * Called when, or after a message has been spoken by the TTS provider
         *
         * @param utteranceId the id provided in the [TTSEngine.speak] method
         */
        fun onDone(utteranceId: String)
    }

    /**
     * Sets a listener for speech events
     *
     * @param listener the listener
     */
    fun setListener(listener: Listener)

    /**
     * Returns if the engine is initialised and ready to speak
     */
    fun isReady(): Boolean

    /**
     * Speaks a message through TTS
     *
     * @param message     the message to speak
     * @param utteranceId an id, referenced in the [Listener] callbacks
     */
    fun speak(message: String, utteranceId: String)

    /**
     * Interrupts the current message being spoken
     */
    fun stop()

    /**
     * Sets the speech pitch for the TextToSpeech engine.
     *
     * @param pitch Speech pitch. {@code 1.0} is the normal pitch,
     *            lower values lower the tone of the synthesized voice,
     *            greater values increase it.
     */
    fun setPitch(pitch: Float)

    /**
     * Sets the speech rate.
     *
     *  @param speechRate Speech rate. {@code 1.0} is the normal speech rate,
     *            lower values slow down the speech ({@code 0.5} is half the normal speech rate),
     *            greater values accelerate it ({@code 2.0} is twice the normal speech rate).
     */
    fun setRate(rate: Float)

}
