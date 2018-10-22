package fr.xgouchet.rehearsal.core.room

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "script_character_join",
        primaryKeys = ["charId", "scriptId"],
        foreignKeys = arrayOf(
                ForeignKey(entity = ScriptModel::class, parentColumns = arrayOf("id"), childColumns = arrayOf("scriptId"), onDelete = ForeignKey.CASCADE),
                ForeignKey(entity = CharacterModel::class, parentColumns = arrayOf("id"), childColumns = arrayOf("charId"), onDelete = ForeignKey.CASCADE)
        )
)
class ScriptCharacterJoin(
        var charId: Int,
        var scriptId: Int
)