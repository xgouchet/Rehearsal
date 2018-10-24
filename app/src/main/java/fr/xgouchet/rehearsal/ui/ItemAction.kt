package fr.xgouchet.rehearsal.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import fr.xgouchet.rehearsal.R

class ItemAction {

    // region VM

    class ViewModel(
            val direction: String = "",
            data: Any? = null
    ) : Item.ViewModel(Item.Type.ACTION, data)

    // endregion

    // region VH

    class ViewHolder(
            itemView: View,
            listener: ItemListener?
    ) : Item.ViewHolder<ViewModel>(itemView) {

        private val directionView: TextView = itemView.findViewById(R.id.direction)

        init {
            if (listener != null) {
                itemView.setOnClickListener { listener(boundItem, ACTION_DEFAULT, null) }
            }
        }

        override fun onBind(item: ViewModel) {
            directionView.text = item.direction
        }

    }

    // endregion

    companion object {
        @JvmStatic
        fun instantiateViewHolder(inflater: LayoutInflater,
                                  parent: ViewGroup,
                                  listener: ItemListener?)
                : ViewHolder {
            val view = inflater.inflate(R.layout.item_action, parent, false)
            return ViewHolder(view, listener)
        }
    }
}
