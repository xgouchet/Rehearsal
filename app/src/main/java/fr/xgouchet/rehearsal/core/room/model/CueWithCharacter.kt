package fr.xgouchet.rehearsal.core.room.model

import androidx.room.Embedded

class CueWithCharacter {
    var cueId : Int = 0
    @Embedded
    var character: CharacterModel? = null
    var type: Int = CueModel.TYPE_UNKNOWN
    var characterExtension: String? = null
    var content: String = ""
    var sceneId : Int = 0
    var position : Int = 0
}
