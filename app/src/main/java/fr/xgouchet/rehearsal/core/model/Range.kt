package fr.xgouchet.rehearsal.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Range (
        val rangeId : Long,
        val rehearsalId : Long,
        val scene : Scene,
        val fromCue : Cue,
        val toCue : Cue
) : Parcelable
