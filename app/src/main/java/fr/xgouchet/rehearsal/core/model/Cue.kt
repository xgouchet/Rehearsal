package fr.xgouchet.rehearsal.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cue(
        val cueId: Long,
        val type: Int,
        val character: Character?,
        val characterExtension: String?,
        val props: List<Prop>,
        val content: String,
        val sceneId: Long,
        val isBookmarked: Boolean,
        val position: Int,
        val note: String?
) : Parcelable
