package fr.xgouchet.rehearsal.voice.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.support.v4.media.session.MediaSessionCompat
import android.view.KeyEvent
import androidx.core.app.NotificationCompat
import fr.xgouchet.rehearsal.R
import fr.xgouchet.rehearsal.core.room.model.CharacterModel
import fr.xgouchet.rehearsal.core.room.model.CueWithCharacter
import timber.log.Timber
import androidx.media.app.NotificationCompat as MediaNotifCompat

class VoiceNotification(val context: Context) {

    private val notifManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
    private var isInForeground = false
    private var lastCue: CueWithCharacter? = null

    // region VoiceNotification

    fun start(service: Service,
              mediaSessionToken: MediaSessionCompat.Token,
              cue: CueWithCharacter) {
        val notification = createNotification(mediaSessionToken, cue, true)
        lastCue = cue

        notifManager?.notify(VOICE_NOTIFICATION_ID, notification)
        switchToForeground(service, notification)
    }

    fun stop(service: Service,
             mediaSessionToken: MediaSessionCompat.Token) {

        val cue = lastCue
        if (cue == null) {
            removeFromForeground(service, true)
        } else {
            val notification = createNotification(mediaSessionToken, cue, false)
            notifManager?.notify(VOICE_NOTIFICATION_ID, notification)
            removeFromForeground(service, false)
        }
    }

    // endregion

    // region Internal

    private fun createNotification(mediaSessionToken: MediaSessionCompat.Token,
                                   cue: CueWithCharacter,
                                   isPlaying: Boolean): Notification {
        val builder = NotificationCompat.Builder(context, VOICE_CHANNEL)

        val character = cue.character
        if (character == null) {
            builder.setSmallIcon(R.drawable.ic_notif_silent)
            buildAnonymousNotification(builder, cue)
        } else {
            buildCharacterNotification(builder, cue, character)
        }

        if (isPlaying) {
            val pendingIntent = createMediaPendingIntent(KeyEvent.KEYCODE_MEDIA_PAUSE)
            builder.addAction(NotificationCompat.Action(R.drawable.ic_pause, "Pause", pendingIntent))
        } else {
            val pendingIntent = createMediaPendingIntent(KeyEvent.KEYCODE_MEDIA_PLAY)
            builder.addAction(NotificationCompat.Action(R.drawable.ic_play, "Resume", pendingIntent))

        }

        val style = MediaNotifCompat.MediaStyle()
                .setShowCancelButton(true)
                .setMediaSession(mediaSessionToken)
                .setShowActionsInCompactView(0)
        builder.setStyle(style)

        return builder.build()
    }

    private fun createMediaPendingIntent(mediaKeyEvent: Int): PendingIntent? {
        val intent = Intent(Intent.ACTION_MEDIA_BUTTON);
        intent.setPackage(context.packageName)
        intent.putExtra(Intent.EXTRA_KEY_EVENT, KeyEvent(KeyEvent.ACTION_DOWN, mediaKeyEvent))

        return PendingIntent.getBroadcast(context, mediaKeyEvent, intent, 0)
    }

    private fun buildCharacterNotification(builder: NotificationCompat.Builder,
                                           cue: CueWithCharacter,
                                           character: CharacterModel) {

        if (cue.characterExtension.isNullOrBlank()) {
            builder.setContentTitle(character.name)
        } else {
            builder.setContentTitle("${character.name} (${cue.characterExtension})")
        }

        if (character.isHidden) {
            builder.setSmallIcon(R.drawable.ic_notif_tragedy)
            builder.setContentText(context.getString(R.string.notif_text_hiddenCue))
        } else {
            builder.setSmallIcon(R.drawable.ic_notif_comedy)
            builder.setContentText(cue.content)
            builder.setStyle(NotificationCompat.BigTextStyle().bigText(cue.content))
        }

    }

    private fun buildAnonymousNotification(builder: NotificationCompat.Builder,
                                           cue: CueWithCharacter) {
        builder.setContentTitle(context.getString(R.string.notif_title_anonymous))

        builder.setContentText(cue.content)
        builder.setStyle(NotificationCompat.BigTextStyle().bigText(cue.content))
    }

    private fun switchToForeground(service: Service, notification: Notification) {
        if (!isInForeground) {
            try {
                Timber.d("#switchToForeground @notification:$notification")
                service.startForeground(VOICE_NOTIFICATION_ID, notification)
                isInForeground = true
            } catch (e: Exception) {
                Timber.d(e, "#error #switchToForeground @notification:$notification")
            }

        }
    }

    private fun removeFromForeground(service: Service, removeNotification: Boolean) {
        Timber.d("#removeFromForeground @removeNotification:$removeNotification")
        service.stopForeground(removeNotification) // apparently even though you are not in foreground you cannot kill your notification through the NotificationManager -> cancel(notificationID)
        isInForeground = false
    }

    companion object {
        const val VOICE_CHANNEL = "voice"
        const val VOICE_NOTIFICATION_ID = 48
    }
}
