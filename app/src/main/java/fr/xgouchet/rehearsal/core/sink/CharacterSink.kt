package fr.xgouchet.rehearsal.core.sink

import android.content.Context
import fr.xgouchet.archx.data.ArchXNoOpDataSink
import fr.xgouchet.rehearsal.core.converter.CharacterDbConverter
import fr.xgouchet.rehearsal.core.model.Character
import fr.xgouchet.rehearsal.core.room.AppDatabase
import io.reactivex.Single

class CharacterSink(context: Context)
    : ArchXNoOpDataSink<Character>() {

    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)

    private val characterDbConverter = CharacterDbConverter()

    override fun updateData(data: Character): Single<Character> {
        return Single.just(data)
                .map {
                    val dbModel = characterDbConverter.write(it)
                    appDatabase.characterDao().update(dbModel)
                    data
                }
    }

}
