package fr.xgouchet.rehearsal.core.source

import android.content.Context
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.rehearsal.core.converter.SceneDbConverter
import fr.xgouchet.rehearsal.core.model.Scene
import fr.xgouchet.rehearsal.core.room.AppDatabase
import io.reactivex.Flowable
import io.reactivex.Single

class AllScenesInScriptSource(
        context: Context,
        val scriptId: Long
) : ArchXDataSource<List<Scene>> {


    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)
    private val sceneDbConverter = SceneDbConverter()

    override fun getData(): Single<List<Scene>> {
        return listenData().firstOrError()
    }

    override fun listenData(): Flowable<List<Scene>> {
        return appDatabase.sceneDao()
                .getAllFromScript(scriptId)
                .map { list -> list.map { sceneDbConverter.read(it, appDatabase) } }
                .onBackpressureLatest()
    }

}
