package fr.xgouchet.rehearsal.home

import android.content.Context
import androidx.lifecycle.LiveData
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.ScriptModel

class HomeDataSource(context: Context)
    : ArchXDataSource<List<ScriptModel>> {

    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)
    private val scriptList: LiveData<List<ScriptModel>> = appDatabase.scriptDao().getAll()

    override fun getData(): LiveData<List<ScriptModel>> {
        return scriptList
    }
}