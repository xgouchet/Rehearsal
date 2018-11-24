package fr.xgouchet.rehearsal.core.sink

import android.content.Context
import fr.xgouchet.archx.data.ArchXNoOpDataSink
import fr.xgouchet.rehearsal.core.model.Character
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.model.CharacterDbModel
import io.reactivex.Single

class CharacterSink(context: Context)
    : ArchXNoOpDataSink<Character>() {

    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)


    override fun updateData(data: Character): Single<Character> {
        return Single.just(data)
                .map {
                    val dbModel = convert(it)
                    appDatabase.characterDao().update(dbModel)
                    data
                }
    }


    private fun convert(character: Character): CharacterDbModel {
        return CharacterDbModel(
                characterId = character.characterId,
                scriptId = character.scriptId,
                name = character.name,
                color = character.color,
                isHidden = character.isHidden,
                ttsEngine = character.ttsEngine,
                ttsPitch = character.ttsPitch,
                ttsRate = character.ttsRate
        )
    }

}
