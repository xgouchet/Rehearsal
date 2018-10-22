package fr.xgouchet.rehearsal.core.room

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "scene_character_join",
        primaryKeys = ["charId", "sceneId"],
        foreignKeys = arrayOf(
                ForeignKey(entity = SceneModel::class, parentColumns = arrayOf("id"), childColumns = arrayOf("sceneId"), onDelete = ForeignKey.CASCADE),
                ForeignKey(entity = CharacterModel::class, parentColumns = arrayOf("id"), childColumns = arrayOf("charId"), onDelete = ForeignKey.CASCADE)
        )
)
class SceneCharacterJoin(
        var charId: Int,
        var sceneId: Int
)