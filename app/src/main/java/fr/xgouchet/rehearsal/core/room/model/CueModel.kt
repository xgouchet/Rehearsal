package fr.xgouchet.rehearsal.core.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "cue",
        foreignKeys = [
            ForeignKey(entity = SceneModel::class, parentColumns = arrayOf("id"), childColumns = arrayOf("sceneId"), onDelete = ForeignKey.CASCADE),
            ForeignKey(entity = CharacterModel::class, parentColumns = arrayOf("id"), childColumns = arrayOf("characterId"), onDelete = ForeignKey.SET_NULL)
        ]
)
data class CueModel(
        @PrimaryKey(autoGenerate = true) var id: Int = 0,
        @ColumnInfo(index = true) var sceneId: Int,
        @ColumnInfo(index = true) var characterId: Int? = null,
        var position: Int,
        var type: Int,
        var characterExtension: String?,
        var content: String
) {

    companion object {
        const val TYPE_UNKNOWN = 0
        const val TYPE_DIALOG = 1
        const val TYPE_ACTION = 2
    }
}
