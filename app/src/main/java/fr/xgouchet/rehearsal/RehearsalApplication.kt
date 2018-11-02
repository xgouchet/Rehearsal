package fr.xgouchet.rehearsal

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import fr.xgouchet.rehearsal.ui.CharacterColor
import fr.xgouchet.rehearsal.voice.service.VoiceNotification
import timber.log.Timber

class RehearsalApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        MultiDex.install(this)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        if (BuildConfig.DEBUG) run {
            Timber.plant(Timber.DebugTree())
        }

        CharacterColor.init(this)

        registerNotificationChannels()
    }

    private fun registerNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_title_voice)
            val descriptionText = getString(R.string.channel_text_voiceDescription)
            val importance = NotificationManager.IMPORTANCE_LOW


            val voiceChannel = NotificationChannel(VoiceNotification.VOICE_CHANNEL, name, importance)
            voiceChannel.description = descriptionText

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(voiceChannel)
        }
    }
}
