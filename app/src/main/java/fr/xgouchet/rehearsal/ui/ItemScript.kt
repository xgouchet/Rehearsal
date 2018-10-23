package fr.xgouchet.rehearsal.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import fr.xgouchet.rehearsal.R

class ItemScript {

    // region VM

    class ViewModel(
            val title: String = "",
            val author: String = "",
            data: Any? = null
    ) : Item.ViewModel(Item.Type.SCRIPT, data)

    // endregion

    // region VH

    class ViewHolder(
            itemView: View,
            listener: ((Any) -> Unit)?
    ) : Item.ViewHolder<ViewModel>(itemView) {

        private val titleView: TextView = itemView.findViewById(R.id.title)
        private val authorView: TextView = itemView.findViewById(R.id.author)

        init {
            if (listener != null) {
                itemView.setOnClickListener { listener(boundItem) }
            }
        }

        @SuppressLint("SetTextI18n")
        override fun onBind(item: ViewModel) {

            titleView.text = MarkdownConverter.parse(item.title)
            authorView.text = MarkdownConverter.parse(item.author)
        }

    }

    // endregion

    companion object {
        @JvmStatic
        fun instantiateViewHolder(inflater: LayoutInflater,
                                  parent: ViewGroup,
                                  listener: ((Any) -> Unit)?)
                : ViewHolder {
            val view = inflater.inflate(R.layout.item_script, parent, false)
            return ViewHolder(view, listener)
        }
    }
}
