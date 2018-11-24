package fr.xgouchet.rehearsal.core.converter

import fr.xgouchet.rehearsal.core.model.Scene
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.model.CueDbModel
import fr.xgouchet.rehearsal.core.room.model.SceneDbModel

class SceneDbConverter
    : DbConverter<Scene, SceneDbModel> {

    override fun write(appModel: Scene): SceneDbModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun read(dataBaseModel: SceneDbModel, appDatabase: AppDatabase): Scene {
        val cuesInScene = appDatabase.cueDao().countTypeInScene(dataBaseModel.sceneId, CueDbModel.TYPE_DIALOG)

        val titlePage = if (dataBaseModel.numbering.isBlank()) {
            dataBaseModel.description
        } else if (dataBaseModel.description.isBlank()) {
            dataBaseModel.numbering
        } else {
            "${dataBaseModel.description} (${dataBaseModel.numbering})"
        }

        return Scene(
                sceneId = dataBaseModel.sceneId,
                scriptId = dataBaseModel.scriptId,
                title = titlePage,
                position = dataBaseModel.position,
                cuesCount = cuesInScene
        )
    }
}
