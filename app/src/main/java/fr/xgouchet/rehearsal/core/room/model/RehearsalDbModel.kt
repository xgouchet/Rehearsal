package fr.xgouchet.rehearsal.core.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import fr.xgouchet.rehearsal.core.room.converter.DateConverter
import java.util.Date

@Entity(tableName = "rehearsal",
        foreignKeys = [
            ForeignKey(entity = ScriptDbModel::class, parentColumns = arrayOf("scriptId"), childColumns = arrayOf("scriptId"), onDelete = ForeignKey.CASCADE)
        ]
)
@TypeConverters(DateConverter::class)
data class RehearsalDbModel(
        @PrimaryKey(autoGenerate = true) var rehearsalId : Long = 0,
        @ColumnInfo(index = true) var scriptId : Long,
        var dueDate: Date = Date()
)
