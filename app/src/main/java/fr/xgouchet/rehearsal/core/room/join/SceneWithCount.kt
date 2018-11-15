package fr.xgouchet.rehearsal.core.room.join

import fr.xgouchet.rehearsal.core.room.model.SceneModel


data class SceneWithCount(
        var sceneId: Int = 0,
        var scriptId: Int,
        var position: Int,
        var description: String,
        var numbering: String,
        var cues: Int
) {
    fun asSceneModel(): SceneModel {
        return SceneModel(
                sceneId = sceneId,
                scriptId = scriptId,
                position = position,
                description = description,
                numbering = numbering
        )
    }
}
