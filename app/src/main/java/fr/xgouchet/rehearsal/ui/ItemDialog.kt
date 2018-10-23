package fr.xgouchet.rehearsal.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import fr.xgouchet.rehearsal.R

class ItemDialog {

    // region VM

    class ViewModel(
            val line: String = "",
            data: Any? = null
    ) : Item.ViewModel(Item.Type.DIALOG, data)

    // endregion

    // region VH

    class ViewHolder(
            itemView: View,
            listener: ((Any) -> Unit)?
    ) : Item.ViewHolder<ViewModel>(itemView) {

        private val lineView: TextView = itemView.findViewById(R.id.line)

        init {
            if (listener != null) {
                itemView.setOnClickListener { listener(boundItem) }
            }
        }

        override fun onBind(item: ViewModel) {
            lineView.text = item.line
        }

    }

    // endregion

    companion object {
        @JvmStatic
        fun instantiateViewHolder(inflater: LayoutInflater,
                                  parent: ViewGroup,
                                  listener: ((Any) -> Unit)?)
                : ViewHolder {
            val view = inflater.inflate(R.layout.item_dialog, parent, false)
            return ViewHolder(view, listener)
        }
    }
}
