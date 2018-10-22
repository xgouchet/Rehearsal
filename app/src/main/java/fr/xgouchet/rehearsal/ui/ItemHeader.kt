package fr.xgouchet.rehearsal.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import fr.xgouchet.rehearsal.R

class ItemHeader {

    // region VM

    class ViewModel(
            val title: String
    ) : Item.ViewModel(Item.Type.HEADER)

    // endregion

    // region VH

    class ViewHolder(
            itemView: View,
            listener: ((Any) -> Unit)?
    ) : Item.ViewHolder<ViewModel>(itemView) {

        private val titleView: TextView = itemView.findViewById(R.id.title)

        init {
            if (listener != null) {
                itemView.setOnClickListener { listener(boundItem) }
            }
        }

        override fun onBind(item: ViewModel) {

            titleView.text = item.title
        }

    }

    // endregion

    companion object {
        @JvmStatic
        fun instantiateViewHolder(inflater: LayoutInflater,
                                  parent: ViewGroup,
                                  listener: ((Any) -> Unit)?)
                : ViewHolder {
            val view = inflater.inflate(R.layout.item_header, parent, false)
            return ViewHolder(view, listener)
        }
    }
}
