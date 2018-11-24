package fr.xgouchet.rehearsal.core.source

import android.content.Context
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.rehearsal.core.model.Script
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.model.ScriptDbModel
import io.reactivex.Flowable
import io.reactivex.Single

class AllScriptsSource(context: Context)
    : ArchXDataSource<List<Script>> {

    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)

    override fun getData(): Single<List<Script>> {
        return listenData().firstOrError()
    }

    override fun listenData(): Flowable<List<Script>> {
        return appDatabase.scriptDao()
                .getAll()
                .map { list -> list.map { convert(it) } }
                .onBackpressureLatest()
    }

    private fun convert(scriptDbModel: ScriptDbModel): Script {
        return Script(
                scriptId = scriptDbModel.scriptId,
                title = scriptDbModel.title,
                author = scriptDbModel.author
        )
    }
}
