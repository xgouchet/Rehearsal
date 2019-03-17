package fr.xgouchet.rehearsal.core.room.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "prop",
        foreignKeys = [
            ForeignKey(entity = ScriptDbModel::class, parentColumns = arrayOf("scriptId"), childColumns = arrayOf("scriptId"), onDelete = ForeignKey.CASCADE)
        ],
        indices = [
            Index(value = ["name"], unique = true),
            Index(value = ["scriptId"])
        ]
)
data class PropDbModel(
        @PrimaryKey(autoGenerate = true) var propId: Long = 0,
        var scriptId: Long,
        var name: String
)
