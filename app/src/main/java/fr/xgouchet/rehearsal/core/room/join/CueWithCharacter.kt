package fr.xgouchet.rehearsal.core.room.join

import androidx.room.Embedded
import fr.xgouchet.rehearsal.core.room.model.CharacterModel
import fr.xgouchet.rehearsal.core.room.model.CueModel

data class CueWithCharacter(
        var cueId: Int = 0,

        @Embedded
        var character: CharacterModel? = null,

        var type: Int = CueModel.TYPE_UNKNOWN,
        var characterExtension: String? = null,
        var content: String = "",
        var sceneId: Int = 0,
        var position: Int = 0,
        var isBookmarked: Boolean = false,
        var note: String? = null
) {

    fun asCueModel(): CueModel {
        return CueModel(
                cueId = cueId,
                sceneId = sceneId,
                characterId = character?.characterId ?: 0,
                position = position,
                type = type,
                characterExtension = characterExtension,
                content = content,
                isBookmarked = isBookmarked,
                note = note
        )
    }
}
