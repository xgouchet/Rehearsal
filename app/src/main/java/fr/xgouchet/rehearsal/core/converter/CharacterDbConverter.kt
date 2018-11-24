package fr.xgouchet.rehearsal.core.converter

import fr.xgouchet.rehearsal.core.model.Character
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.model.CharacterDbModel

class CharacterDbConverter
    : DbConverter<Character, CharacterDbModel> {
    override fun write(appModel: Character): CharacterDbModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun read(dataBaseModel: CharacterDbModel, appDatabase: AppDatabase): Character {
        return Character(
                characterId = dataBaseModel.characterId,
                scriptId = dataBaseModel.scriptId,
                name = dataBaseModel.name,
                color = dataBaseModel.color,
                isHidden = dataBaseModel.isHidden,
                ttsEngine = dataBaseModel.ttsEngine,
                ttsPitch = dataBaseModel.ttsPitch,
                ttsRate = dataBaseModel.ttsRate
        )
    }
}
