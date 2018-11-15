package fr.xgouchet.rehearsal.core.room.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "range",
        foreignKeys = [
            ForeignKey(entity = ScheduleModel::class, parentColumns = arrayOf("scheduleId"), childColumns = arrayOf("scheduleId"), onDelete = ForeignKey.CASCADE)
        ]
)
@Parcelize
data class RangeModel(
        @PrimaryKey(autoGenerate = true) var rangeId: Int = 0,
        @Embedded(prefix = "start_") var startCue: CueModel? = null,
        @Embedded(prefix = "end_") var endCue: CueModel? = null,
        @ColumnInfo(index = true) var scheduleId: Int? = null
) : Parcelable
