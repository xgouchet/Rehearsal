package fr.xgouchet.rehearsal.core.sink

import android.content.Context
import fr.xgouchet.archx.data.ArchXNoOpDataSink
import fr.xgouchet.rehearsal.core.converter.CueDbConverter
import fr.xgouchet.rehearsal.core.model.Cue
import fr.xgouchet.rehearsal.core.room.AppDatabase
import io.reactivex.Observable
import io.reactivex.Single

class CuesSink(context: Context) : ArchXNoOpDataSink<List<Cue>>() {

    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)
    private val cueDbConverter = CueDbConverter()

    override fun createData(data: List<Cue>): Single<List<Cue>> {
        return Observable.fromIterable(data)
                .map {
                    val dbModel = cueDbConverter.write(it)
                    val id = appDatabase.cueDao().insert(dbModel)
                    it.copy(cueId = id)
                }
                .toList()
    }

    override fun updateData(data: List<Cue>): Single<List<Cue>> {
        return Observable.fromIterable(data)
                .map { cueDbConverter.write(it) }
                .toList()
                .map {
                    appDatabase.cueDao().updateAll(it)
                    data
                }
    }

    override fun deleteData(data: List<Cue>): Single<List<Cue>> {
        return Observable.fromIterable(data)
                .map { cueDbConverter.write(it) }
                .toList()
                .map {
                    appDatabase.cueDao().deleteAll(it)
                    data
                }
    }

}
