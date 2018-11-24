package fr.xgouchet.rehearsal.core.converter

import fr.xgouchet.rehearsal.core.model.Range
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.model.RangeDbModel

class RangeDbConverter
    : DbConverter<Range, RangeDbModel> {

    private val cueDbConverter = CueDbConverter()
    private val sceneDbConverter = SceneDbConverter()

    override fun write(appModel: Range): RangeDbModel {
        return RangeDbModel(
                rangeId = appModel.rangeId,
                rehearsalId = appModel.rehearsalId,
                sceneId = appModel.scene.sceneId,
                startCueId = appModel.fromCue.cueId,
                endCueId = appModel.toCue.cueId
        )
    }

    override fun read(dataBaseModel: RangeDbModel, appDatabase: AppDatabase): Range {
        val sceneDbModel = appDatabase.sceneDao().get(dataBaseModel.sceneId)
        checkNotNull(sceneDbModel)

        val startCue = appDatabase.cueDao().get(dataBaseModel.startCueId)
        checkNotNull(startCue)

        val endCue = appDatabase.cueDao().get(dataBaseModel.endCueId)
        checkNotNull(endCue)

        return Range(
                rangeId = dataBaseModel.rangeId,
                rehearsalId = dataBaseModel.rehearsalId,
                scene = sceneDbConverter.read(sceneDbModel, appDatabase),
                fromCue = cueDbConverter.read(startCue, appDatabase),
                toCue = cueDbConverter.read(endCue, appDatabase)
        )
    }
}
