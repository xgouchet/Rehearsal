package fr.xgouchet.rehearsal.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import fr.xgouchet.rehearsal.R

class ItemEmpty {

    // region VM

    class ViewModel(
            val id: Long,
            val title: String = "",
            val body: String = "",
            val data: Any? = null
    ) : Item.ViewModel() {
        override fun getItemType() = Item.Type.EMPTY

        override fun getItemStableId() = id

        override fun getItemData(): Any? = data
    }

    // endregion

    // region VH

    class ViewHolder(itemView: View)
        : Item.ViewHolder<ViewModel>(itemView) {

        private val titleView: TextView = itemView.findViewById(R.id.title)
        private val bodyView: TextView = itemView.findViewById(R.id.body)

        override fun onBind(item: ViewModel) {

            titleView.text = item.title
            bodyView.text = item.body
        }

    }

    // endregion

    companion object {
        @JvmStatic
        fun instantiateViewHolder(inflater: LayoutInflater,
                                  parent: ViewGroup)
                : ViewHolder {
            val view = inflater.inflate(R.layout.item_empty, parent, false)
            return ViewHolder(view)
        }
    }
}
