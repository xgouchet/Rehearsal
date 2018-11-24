package fr.xgouchet.rehearsal.core.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "script")
data class ScriptDbModel(
        @PrimaryKey(autoGenerate = true) var scriptId : Long = 0,
        var title: String,
        var author: String
        // TODO add other metadata
)
