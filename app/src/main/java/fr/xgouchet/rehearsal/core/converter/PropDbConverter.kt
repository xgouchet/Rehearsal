package fr.xgouchet.rehearsal.core.converter

import fr.xgouchet.rehearsal.core.model.Prop
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.model.PropDbModel

class PropDbConverter
    : DbConverter<Prop, PropDbModel> {

    private val sceneDbConverter = SceneDbConverter()

    override fun write(appModel: Prop): PropDbModel {
        return PropDbModel(
                propId = appModel.propId,
                scriptId = appModel.scriptId,
                name = appModel.name
        )
    }

    override fun read(dataBaseModel: PropDbModel, appDatabase: AppDatabase): Prop {
        val cueDbModels = appDatabase.propDao().getCuesUsingProp(dataBaseModel.propId)
        val scenes = cueDbModels.asSequence()
                .distinctBy { it.sceneId }
                .mapNotNull {
                    appDatabase.sceneDao().get(it.sceneId)?.let { sceneDbModel ->
                        sceneDbConverter.read(sceneDbModel, appDatabase)
                    }
                }
                .sortedBy { it.position }
                .toList()

        return Prop(
                propId = dataBaseModel.propId,
                scriptId = dataBaseModel.scriptId,
                name = dataBaseModel.name,
                scenes = scenes
        )
    }
}
