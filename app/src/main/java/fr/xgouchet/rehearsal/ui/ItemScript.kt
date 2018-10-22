package fr.xgouchet.rehearsal.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import fr.xgouchet.rehearsal.R

class ItemScript {
    // region VM

    class ViewModel(
            val title: String
    ) : Item.ViewModel(Item.Type.SCRIPT)

    // endregion

    // region VH

    class ViewHolder(itemView: View)
        : Item.ViewHolder<ViewModel>(itemView) {

        private val titleView: TextView = itemView.findViewById(R.id.title)

        override fun onBind(item: ViewModel) {

            titleView.text = item.title
        }

    }

    // endregion

    companion object {
        @JvmStatic
        fun instantiateViewHolder(inflater: LayoutInflater,
                                  parent: ViewGroup)
                : ViewHolder {
            val view = inflater.inflate(R.layout.item_script, parent, false)
            return ViewHolder(view)
        }
    }
}
