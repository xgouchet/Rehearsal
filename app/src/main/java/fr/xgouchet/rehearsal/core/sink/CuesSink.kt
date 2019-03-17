package fr.xgouchet.rehearsal.core.sink

import android.content.Context
import fr.xgouchet.archx.data.ArchXNoOpDataSink
import fr.xgouchet.rehearsal.core.converter.CueDbConverter
import fr.xgouchet.rehearsal.core.model.Cue
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.model.PropCueDbModel
import io.reactivex.Observable
import io.reactivex.Single

class CuesSink(context: Context) : ArchXNoOpDataSink<List<Cue>>() {

    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)
    private val cueDbConverter = CueDbConverter()

    override fun createData(data: List<Cue>): Single<List<Cue>> {
        return Observable.fromIterable(data)
                .map { cue ->
                    val dbModel = cueDbConverter.write(cue)
                    val id = appDatabase.cueDao().insert(dbModel)
                    cue.props.forEach { prop ->
                        appDatabase.propDao().insert(PropCueDbModel(propId = prop.propId, cueId = cue.cueId))
                    }
                    cue.copy(cueId = id)
                }
                .toList()
    }

    override fun updateData(data: List<Cue>): Single<List<Cue>> {
        return Observable.fromIterable(data)
                .map { cue ->
                    val dbModel = cueDbConverter.write(cue)
                    cue.props.forEach { prop ->
                        val existingLink = appDatabase.propDao().getLink(propId = prop.propId, cueId = cue.cueId)
                        if (existingLink == null) {
                            appDatabase.propDao().insert(PropCueDbModel(propId = prop.propId, cueId = cue.cueId))
                        }
                    }
                    val remainingLinks = appDatabase.propDao().getAllLinksFromCue(cueId = cue.cueId)
                    remainingLinks.forEach { propLink ->
                        if (cue.props.firstOrNull { it.propId == propLink.propId } == null) {
                            appDatabase.propDao().delete(propLink)
                        }
                    }
                    appDatabase.cueDao().update(dbModel)
                    cue
                }
                .toList()

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
