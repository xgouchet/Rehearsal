package fr.xgouchet.rehearsal.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Script(
        val scriptId : Long,
        val title: String,
        val author: String
) : Parcelable
