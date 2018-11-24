package fr.xgouchet.rehearsal

import com.facebook.stetho.Stetho

class RehearsalDebugApplication : RehearsalApplication() {

    override fun onCreate() {
        super.onCreate()


        Stetho.initializeWithDefaults(this)
    }

}
