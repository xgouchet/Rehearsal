package fr.xgouchet.rehearsal.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Character (
        val characterId : Long,
        val scriptId : Long,
        val name: String,
        val color : Int,
        val isHidden : Boolean,
        var ttsEngine: String?,
        var ttsPitch: Float,
        var ttsRate: Float
) : Parcelable
