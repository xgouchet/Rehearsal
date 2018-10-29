package fr.xgouchet.rehearsal

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import fr.xgouchet.rehearsal.ui.CharacterColor
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

    }
}
