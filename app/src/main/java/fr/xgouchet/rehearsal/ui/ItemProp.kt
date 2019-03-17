package fr.xgouchet.rehearsal.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import fr.xgouchet.rehearsal.R

class ItemProp {

    // region VM

    class ViewModel(
            val id: Long,
            val title: String = "",
            val data: Any? = null
    ) : Item.ViewModel() {
        override fun getItemType() = Item.Type.PROP

        override fun getItemStableId() = id

        override fun getItemData(): Any? = data
    }

    // endregion

    // region VH

    class ViewHolder(
            itemView: View,
            listener: ItemListener
    ) : Item.ViewHolder<ViewModel>(itemView) {

        private val titleView: TextView = itemView.findViewById(R.id.title)

        init {
            itemView.setOnClickListener { listener(boundItem, ACTION_DEFAULT, null) }
        }

        @SuppressLint("SetTextI18n")
        override fun onBind(item: ViewModel) {
            titleView.text = item.title
        }

    }

    // endregion

    companion object {
        @JvmStatic
        fun instantiateViewHolder(inflater: LayoutInflater,
                                  parent: ViewGroup,
                                  listener: ItemListener)
                : ViewHolder {
            val view = inflater.inflate(R.layout.item_prop, parent, false)
            return ViewHolder(view, listener)
        }
    }
}
