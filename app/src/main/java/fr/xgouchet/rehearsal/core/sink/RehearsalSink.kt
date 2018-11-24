package fr.xgouchet.rehearsal.core.sink

import android.content.Context
import fr.xgouchet.archx.data.ArchXNoOpDataSink
import fr.xgouchet.rehearsal.core.model.Rehearsal
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.model.RehearsalDbModel
import io.reactivex.Single

class RehearsalSink(context: Context)
    : ArchXNoOpDataSink<Rehearsal>() {

    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)

    override fun createData(data: Rehearsal): Single<Rehearsal> {
        return Single.just(data)
                .map {
                    val dbModel = convert(it)
                    val id = appDatabase.rehearsalDao().insert(dbModel)
                    data.copy(rehearsalId = id)
                }
    }


    override fun deleteData(data: Rehearsal): Single<Rehearsal> {
        return Single.just(data)
                .map {
                    val id = appDatabase.rehearsalDao().deleteById(data.rehearsalId)
                    data
                }
    }

    private fun convert(rehearsal: Rehearsal): RehearsalDbModel {
        return RehearsalDbModel(
                rehearsalId = rehearsal.rehearsalId,
                scriptId = rehearsal.scriptId,
                dueDate = rehearsal.dueDate
        )
    }

}
