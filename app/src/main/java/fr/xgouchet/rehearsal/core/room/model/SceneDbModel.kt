package fr.xgouchet.rehearsal.core.room.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "scene",
        foreignKeys = [
            ForeignKey(entity = ScriptDbModel::class, parentColumns = arrayOf("scriptId"), childColumns = arrayOf("scriptId"), onDelete = ForeignKey.CASCADE)
        ]
)
@Parcelize
data class SceneDbModel(
        @PrimaryKey(autoGenerate = true) var sceneId : Long = 0,
        @ColumnInfo(index = true) var scriptId : Long,
        var position: Int,
        var description: String,
        var numbering: String
) : Parcelable
