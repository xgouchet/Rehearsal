package fr.xgouchet.rehearsal.core.source

import android.content.Context
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.rehearsal.core.converter.CharacterDbConverter
import fr.xgouchet.rehearsal.core.model.Character
import fr.xgouchet.rehearsal.core.room.AppDatabase
import io.reactivex.Flowable
import io.reactivex.Single

class AllCharactersInSceneSource(
        context: Context,
        val sceneId: Long
) : ArchXDataSource<List<Character>> {


    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)
    private val characterDbConverter = CharacterDbConverter()

    override fun getData(): Single<List<Character>> {
        return listenData().firstOrError()
    }

    override fun listenData(): Flowable<List<Character>> {
        return appDatabase.characterDao()
                .getAllFromScene(sceneId)
                .map { list -> list.map { characterDbConverter.read(it, appDatabase) } }
                .onBackpressureLatest()
    }

}
