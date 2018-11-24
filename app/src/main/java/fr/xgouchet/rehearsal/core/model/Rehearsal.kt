package fr.xgouchet.rehearsal.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class Rehearsal(
        val rehearsalId: Long,
        val scriptId: Long,
        val dueDate: Date
) : Parcelable
