package fr.xgouchet.rehearsal.core.room.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "range",
        indices = [Index("sceneId"), Index("startCueId"), Index("endCueId"), Index("rehearsalId")],
        foreignKeys = [
            ForeignKey(entity = RehearsalDbModel::class, parentColumns = arrayOf("rehearsalId"), childColumns = arrayOf("rehearsalId"), onDelete = ForeignKey.CASCADE),
            ForeignKey(entity = SceneDbModel::class, parentColumns = arrayOf("sceneId"), childColumns = arrayOf("sceneId"), onDelete = ForeignKey.CASCADE),
            ForeignKey(entity = CueDbModel::class, parentColumns = arrayOf("cueId"), childColumns = arrayOf("startCueId"), onDelete = ForeignKey.CASCADE),
            ForeignKey(entity = CueDbModel::class, parentColumns = arrayOf("cueId"), childColumns = arrayOf("endCueId"), onDelete = ForeignKey.CASCADE)
        ]
)
@Parcelize
data class RangeDbModel(
        @PrimaryKey(autoGenerate = true) var rangeId: Long = 0,
        var sceneId: Long,
        var startCueId: Long,
        var endCueId: Long,
        var rehearsalId: Long
) : Parcelable
