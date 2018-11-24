package fr.xgouchet.rehearsal.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Scene(
        val sceneId : Long,
        val scriptId : Long,
        val position: Int,
        val title: String,
        val cuesCount: Int
) : Parcelable
