package fr.xgouchet.rehearsal.core.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "character",
        foreignKeys = [
            ForeignKey(entity = ScriptModel::class, parentColumns = arrayOf("id"), childColumns = arrayOf("scriptId"), onDelete = ForeignKey.CASCADE)
        ]
)
data class CharacterModel(
        @PrimaryKey(autoGenerate = true) var id: Int = 0,
        @ColumnInfo(index = true) var scriptId: Int,
        var name: String,
        var isSelected: Boolean = false
)
