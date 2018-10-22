package fr.xgouchet.rehearsal.core.room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "scene")
data class SceneModel(
        @PrimaryKey(autoGenerate = true) var id: Int,
        var title: String)
