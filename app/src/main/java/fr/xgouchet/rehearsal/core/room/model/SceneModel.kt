package fr.xgouchet.rehearsal.core.room.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "scene",
        foreignKeys = [
            ForeignKey(entity = ScriptModel::class, parentColumns = arrayOf("scriptId"), childColumns = arrayOf("scriptId"), onDelete = ForeignKey.CASCADE)
        ]
)
@Parcelize
data class SceneModel(
        @PrimaryKey(autoGenerate = true) var sceneId: Int = 0,
        @ColumnInfo(index = true) var scriptId: Int,
        var position: Int,
        var description: String,
        var numbering: String
) : Parcelable
