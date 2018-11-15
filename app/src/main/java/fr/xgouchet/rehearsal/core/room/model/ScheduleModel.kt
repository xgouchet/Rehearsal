package fr.xgouchet.rehearsal.core.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Date

@Entity(tableName = "schedule",
        foreignKeys = [
            ForeignKey(entity = ScriptModel::class, parentColumns = arrayOf("scriptId"), childColumns = arrayOf("scriptId"), onDelete = ForeignKey.CASCADE)
        ]
)
@TypeConverters(DateConverter::class)
data class ScheduleModel(
        @PrimaryKey(autoGenerate = true) var scheduleId: Int = 0,
        @ColumnInfo(index = true) var scriptId: Int? = null,
        var dueDate: Date = Date(),
        var note : String? = null
)
