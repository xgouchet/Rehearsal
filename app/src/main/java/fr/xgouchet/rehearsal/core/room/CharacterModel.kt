package fr.xgouchet.rehearsal.core.room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "character")
data class CharacterModel(
        @PrimaryKey(autoGenerate = true) var id: Int,
        var name: String,
        var description: String)
