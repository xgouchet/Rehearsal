package fr.xgouchet.rehearsal.core.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "character",
        foreignKeys = [
            ForeignKey(entity = ScriptDbModel::class, parentColumns = arrayOf("scriptId"), childColumns = arrayOf("scriptId"), onDelete = ForeignKey.CASCADE)
        ]
)
data class CharacterDbModel(
        @PrimaryKey(autoGenerate = true) var characterId : Long = 0,
        @ColumnInfo(index = true) var scriptId : Long,
        var name: String,
        var isHidden: Boolean = false,
        var color: Int = -1,
        var ttsEngine: String? = null,
        var ttsPitch: Float = 1.0f,
        var ttsRate: Float = 1.0f
)
