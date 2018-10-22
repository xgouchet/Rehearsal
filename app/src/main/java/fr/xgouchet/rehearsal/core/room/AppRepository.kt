package fr.xgouchet.rehearsal.core.room

import android.app.Application
import androidx.lifecycle.LiveData

class AppRepository(app: Application) {

    private val scriptDao: ScriptDAO
    private val scriptLiveData: LiveData<List<ScriptModel>>

    init {
        val appDatabase = AppDatabase.getInstance(app)!!
        scriptDao = appDatabase.scriptDao()
        scriptLiveData = scriptDao.getAll()
    }

    fun getScripts(): LiveData<List<ScriptModel>> {
        return scriptLiveData
    }
}