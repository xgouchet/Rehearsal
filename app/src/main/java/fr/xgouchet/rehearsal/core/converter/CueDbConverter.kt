package fr.xgouchet.rehearsal.core.converter

import fr.xgouchet.rehearsal.core.model.Cue
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.model.CueDbModel

class CueDbConverter
    : DbConverter<Cue, CueDbModel> {

    private val characterDbConverter = CharacterDbConverter()
    private val propDbConverter = PropDbConverter()

    override fun write(appModel: Cue): CueDbModel {
        return CueDbModel(
                cueId = appModel.cueId,
                type = appModel.type,
                characterId = appModel.character?.characterId,
                characterExtension = appModel.characterExtension,
                position = appModel.position,
                sceneId = appModel.sceneId,
                note = appModel.note,
                content = appModel.content,
                isBookmarked = appModel.isBookmarked,
                props = appModel.props.size
        )
    }

    override fun read(dataBaseModel: CueDbModel, appDatabase: AppDatabase): Cue {
        val characterDbModel = appDatabase.characterDao().get(dataBaseModel.characterId ?: -1)
        val character = if (characterDbModel == null) null else characterDbConverter.read(characterDbModel, appDatabase)
        val propDbModels = appDatabase.propDao().getAllFromCue(dataBaseModel.cueId)
        val props = propDbModels.map { propDbConverter.read(it, appDatabase) }

        return Cue(
                cueId = dataBaseModel.cueId,
                type = dataBaseModel.type,
                character = character,
                characterExtension = dataBaseModel.characterExtension,
                props = props,
                position = dataBaseModel.position,
                sceneId = dataBaseModel.sceneId,
                note = dataBaseModel.note,
                content = dataBaseModel.content,
                isBookmarked = dataBaseModel.isBookmarked
        )
    }
}
