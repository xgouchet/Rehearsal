package fr.xgouchet.rehearsal.voice.service

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.os.HandlerThread
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.MediaSessionManager
import fr.xgouchet.rehearsal.core.RuntimeSchedulerProvider
import fr.xgouchet.rehearsal.core.model.Cue
import fr.xgouchet.rehearsal.core.source.AllCuesInSceneSource
import fr.xgouchet.rehearsal.voice.ipc.MessageProtocol
import fr.xgouchet.rehearsal.voice.tts.AndroidTTSEngine
import timber.log.Timber

class VoiceService
    : Service(),
        SceneReader.Listener,
        VoiceMediaSessionCallback.Delegate {

    private val listeningMessengers: MutableList<Messenger> = mutableListOf()
    private lateinit var handlerThread: HandlerThread
    private lateinit var inMessenger: Messenger
    private lateinit var messageHandler: VoiceServiceMessageHandler

    private lateinit var voiceSceneReader: VoiceSceneReader
    private lateinit var voiceNotification: VoiceNotification
    private var isPlaying: Boolean = false


    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionMgr: MediaSessionManager
    private lateinit var mediaSessionCallback: MediaSessionCompat.Callback

    // region Service

    override fun onCreate() {
        super.onCreate()
        Timber.d("#service #created")

        handlerThread = HandlerThread("VoiceService")
        handlerThread.start()

        messageHandler = VoiceServiceMessageHandler(this)

        inMessenger = Messenger(messageHandler)

        val dataSourceProvider = { sceneId: Long -> AllCuesInSceneSource(applicationContext, sceneId) }
        voiceSceneReader = VoiceSceneReader(dataSourceProvider, this, RuntimeSchedulerProvider) {
            AndroidTTSEngine(it, this@VoiceService)
        }
        voiceNotification = VoiceNotification(this)

        mediaSessionMgr = MediaSessionManager.getSessionManager(this)
        mediaSessionCallback = VoiceMediaSessionCallback(this)

        val eventReceiver = ComponentName(applicationContext, VoiceMediaButtonReceiver::class.java)
        mediaSession = MediaSessionCompat(this, "Rehearsal", eventReceiver, null)
        mediaSession.setCallback(mediaSessionCallback, messageHandler)
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
    }

    @Suppress("DEPRECATION", "OverridingDeprecatedMember")
    override fun onStart(intent: Intent, startId: Int) {
        super.onStart(intent, startId)
        Timber.d("#service #started #legay")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Timber.d("#service #started @startId:$startId @flags:$flags @intent:$intent")

        if (intent == null) {
            Timber.w("#service was stopped and automatically restarted by the system. Stopping self now.")
            stopSelf()
        } else {
            when (intent.action) {
                Intent.ACTION_MEDIA_BUTTON -> mediaSessionCallback.onMediaButtonEvent(intent)
                AudioManager.ACTION_AUDIO_BECOMING_NOISY -> onPause()
            }
        }

        return Service.START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        Timber.d("#service #bound")
        return inMessenger.binder
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("#service #destroyed")
        messageHandler.removeCallbacksAndMessages(null)
        handlerThread.quit()
    }

    // endregion

    // region VoiceService

    fun registerListener(listener: Messenger) {
        listeningMessengers.add(listener)
    }

    fun playSceneFromCue(sceneId: Long, cueId: Long) {
        voiceSceneReader.playSceneFromCue(sceneId, cueId)
    }

    fun pauseScene() {
        voiceSceneReader.pauseScene()
    }

    // endregion

    // region SceneReader.Listener

    override fun readingCue(cue: Cue) {
        isPlaying = true
        voiceNotification.start(this, mediaSession.sessionToken, cue)

        sendMessage {
            val message = Message.obtain(null, MessageProtocol.MSG_READING_CUE)

            val bundle = Bundle(1)
            bundle.putLong(MessageProtocol.EXTRA_CUE_ID, cue.cueId)
            message.data = bundle

            message
        }
    }

    override fun stopped() {
        isPlaying = false
        voiceNotification.stop(this, mediaSession.sessionToken)

        sendMessage { Message.obtain(null, MessageProtocol.MSG_STOPPED) }
    }

    // endregion

    // region VoiceMediaSessionCallback.Delegate

    override fun isCurrentlyPlaying(): Boolean {
        return isPlaying
    }

    override fun onPause() {
        pauseScene()
    }

    override fun onResume() {
        voiceSceneReader.resume()
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

    companion object {
        @JvmStatic
        fun buildIntent(context: Context, intent: Intent): Intent {
            val serviceIntent = Intent(intent)
            serviceIntent.setClass(context, VoiceService::class.java)
            return serviceIntent
        }
    }
}
