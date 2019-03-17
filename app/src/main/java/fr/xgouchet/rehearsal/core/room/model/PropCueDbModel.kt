package fr.xgouchet.rehearsal.core.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "propcue",
        foreignKeys = [
            ForeignKey(entity = CueDbModel::class, parentColumns = arrayOf("cueId"), childColumns = arrayOf("cueId"), onDelete = ForeignKey.CASCADE),
            ForeignKey(entity = PropDbModel::class, parentColumns = arrayOf("propId"), childColumns = arrayOf("propId"), onDelete = ForeignKey.CASCADE)
        ]
)
data class PropCueDbModel(
        @PrimaryKey(autoGenerate = true) var linkId: Long = 0,
        @ColumnInfo(index = true) var cueId: Long,
        @ColumnInfo(index = true) var propId: Long
)
