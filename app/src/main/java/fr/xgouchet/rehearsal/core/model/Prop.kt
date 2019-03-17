package fr.xgouchet.rehearsal.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Prop(
        val propId: Long,
        val scriptId: Long,
        val name: String,
        val scenes : List<Scene>
) : Parcelable {
    override fun toString(): String {
        return name
    }
}
