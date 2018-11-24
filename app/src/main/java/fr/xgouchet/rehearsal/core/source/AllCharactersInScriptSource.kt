package fr.xgouchet.rehearsal.core.source

import android.content.Context
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.rehearsal.core.converter.CharacterDbConverter
import fr.xgouchet.rehearsal.core.model.Character
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.model.CharacterDbModel
import io.reactivex.Flowable
import io.reactivex.Single

class AllCharactersInScriptSource(
        context: Context,
        private val scriptId : Long
) : ArchXDataSource<List<Character>> {


    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)
    private val characterDbConverter = CharacterDbConverter()

    override fun getData(): Single<List<Character>> {
        return listenData().firstOrError()
    }

    override fun listenData(): Flowable<List<Character>> {
        return appDatabase.characterDao()
                .getAllFromScript(scriptId)
                .map { list -> list.map { characterDbConverter.read(it, appDatabase) } }
                .onBackpressureLatest()
    }

}
