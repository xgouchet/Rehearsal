package fr.xgouchet.rehearsal.core.source

import android.content.Context
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.rehearsal.core.model.Rehearsal
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.model.RehearsalDbModel
import io.reactivex.Flowable
import io.reactivex.Single

class AllRehearsalsInScriptSource(
        context: Context,
        private val scriptId : Long
) : ArchXDataSource<List<Rehearsal>> {


    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)

    override fun getData(): Single<List<Rehearsal>> {
        return listenData().firstOrError()
    }

    override fun listenData(): Flowable<List<Rehearsal>> {
        return appDatabase.rehearsalDao()
                .getAllFromScript(scriptId)
                .map { list -> list.map { convert(it) } }
                .onBackpressureLatest()
    }

    private fun convert(setDbModel: RehearsalDbModel): Rehearsal {
        return Rehearsal(
                rehearsalId = setDbModel.rehearsalId,
                scriptId = setDbModel.scriptId,
                dueDate = setDbModel.dueDate
        )
    }

}
