package fr.xgouchet.rehearsal.core.room.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "cue",
        foreignKeys = [
            ForeignKey(entity = SceneDbModel::class, parentColumns = arrayOf("sceneId"), childColumns = arrayOf("sceneId"), onDelete = ForeignKey.CASCADE),
            ForeignKey(entity = CharacterDbModel::class, parentColumns = arrayOf("characterId"), childColumns = arrayOf("characterId"), onDelete = ForeignKey.SET_NULL)
        ]
)
@Parcelize
data class CueDbModel(
        @PrimaryKey(autoGenerate = true) var cueId: Long = 0,
        @ColumnInfo(index = true) var sceneId: Long,
        @ColumnInfo(index = true) var characterId: Long? = null,
        var position: Int,
        var type: Int,
        var characterExtension: String?,
        var content: String,
        var isBookmarked: Boolean,
        var note: String? = null,
        var props:Int = 0
) : Parcelable {

    companion object {
        const val TYPE_UNKNOWN = 0
        const val TYPE_DIALOG = 1
        const val TYPE_ACTION = 2
        const val TYPE_LYRICS = 3
    }
}
