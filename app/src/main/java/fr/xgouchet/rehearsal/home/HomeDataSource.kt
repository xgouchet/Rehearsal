package fr.xgouchet.rehearsal.home

import android.content.Context
import androidx.lifecycle.LiveData
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.model.ScriptModel

class HomeDataSource(context: Context)
    : HomeContract.DataSource {

    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)
    private val scriptList: LiveData<List<ScriptModel>> = appDatabase.scriptDao().getAll()

    override fun getData(): LiveData<List<ScriptModel>> {
        return scriptList
    }
}
