package fr.xgouchet.rehearsal.core.room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "script")
data class ScriptModel(
        @PrimaryKey(autoGenerate = true) var id: Int,
        var title: String,
        var author: String
)
