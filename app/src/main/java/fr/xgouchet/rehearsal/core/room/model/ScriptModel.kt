package fr.xgouchet.rehearsal.core.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "script")
data class ScriptModel(
        @PrimaryKey(autoGenerate = true) var id: Int = 0,
        var title: String
        // TODO add other metadata
)
