package fr.xgouchet.rehearsal.core.converter

import fr.xgouchet.rehearsal.core.model.Character
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.model.CharacterDbModel
import fr.xgouchet.rehearsal.core.room.model.CueDbModel

class CharacterDbConverter
    : DbConverter<Character, CharacterDbModel> {

    override fun write(appModel: Character): CharacterDbModel {
        return CharacterDbModel(
                characterId = appModel.characterId,
                scriptId = appModel.scriptId,
                name = appModel.name,
                color = appModel.color,
                isHidden = appModel.isHidden,
                ttsEngine = appModel.ttsEngine,
                ttsPitch = appModel.ttsPitch,
                ttsRate = appModel.ttsRate
        )
    }

    override fun read(dataBaseModel: CharacterDbModel, appDatabase: AppDatabase): Character {
        val cuesCount = appDatabase.cueDao().countTypeWithCharacter(dataBaseModel.characterId, CueDbModel.TYPE_DIALOG)
        return Character(
                characterId = dataBaseModel.characterId,
                scriptId = dataBaseModel.scriptId,
                name = dataBaseModel.name,
                color = dataBaseModel.color,
                isHidden = dataBaseModel.isHidden,
                ttsEngine = dataBaseModel.ttsEngine,
                ttsPitch = dataBaseModel.ttsPitch,
                ttsRate = dataBaseModel.ttsRate,
                cuesCount = cuesCount
        )
    }
}
