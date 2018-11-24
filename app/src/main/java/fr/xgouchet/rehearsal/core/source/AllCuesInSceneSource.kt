package fr.xgouchet.rehearsal.core.source

import android.content.Context
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.rehearsal.core.converter.CueDbConverter
import fr.xgouchet.rehearsal.core.model.Cue
import fr.xgouchet.rehearsal.core.room.AppDatabase
import io.reactivex.Flowable
import io.reactivex.Single

class AllCuesInSceneSource(
        context: Context,
        val sceneId: Long
) : ArchXDataSource<List<Cue>> {

    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)
    private val cueDbConverter = CueDbConverter()

    override fun getData(): Single<List<Cue>> {
        return listenData().firstOrError()
    }

    override fun listenData(): Flowable<List<Cue>> {
        return appDatabase.cueDao().getAllInScene(sceneId)
                .map { list -> list.map { cueDbConverter.read(it, appDatabase) } }
                .onBackpressureLatest()
    }

}
