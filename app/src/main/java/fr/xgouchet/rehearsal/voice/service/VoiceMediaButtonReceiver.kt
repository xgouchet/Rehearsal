package fr.xgouchet.rehearsal.voice.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class VoiceMediaButtonReceiver
    : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_MEDIA_BUTTON) {
            // Just forward the intent to the VoiceService
            context.startService(VoiceService.buildIntent(context, intent))
        }
    }

}
