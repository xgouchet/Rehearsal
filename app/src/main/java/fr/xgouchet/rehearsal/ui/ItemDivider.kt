package fr.xgouchet.rehearsal.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.xgouchet.rehearsal.R

class ItemDivider {

    // region VM

    class ViewModel(id: Long) : Item.ViewModel(Item.Type.DIVIDER, id, null)

    // endregion

    // region VH

    class ViewHolder(
            itemView: View
    ) : Item.ViewHolder<ViewModel>(itemView) {

        override fun onBind(item: ViewModel) {
        }

    }

    // endregion

    companion object {
        @JvmStatic
        fun instantiateViewHolder(inflater: LayoutInflater,
                                  parent: ViewGroup)
                : ViewHolder {
            val view = inflater.inflate(R.layout.item_divider, parent, false)
            return ViewHolder(view)
        }
    }
}
