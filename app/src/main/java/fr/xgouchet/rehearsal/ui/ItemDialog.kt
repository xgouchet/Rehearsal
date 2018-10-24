package fr.xgouchet.rehearsal.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import fr.xgouchet.rehearsal.R

class ItemDialog {

    // region VM

    class ViewModel(
            val line: String = "",
            val hidden: Boolean = false,
            data: Any? = null
    ) : Item.ViewModel(Item.Type.DIALOG, data)

    // endregion

    // region VH

    class ViewHolder(
            itemView: View,
            listener: ItemListener?
    ) : Item.ViewHolder<ViewModel>(itemView) {

        private val lineView: TextView = itemView.findViewById(R.id.line)
        private val hidingView: View = itemView.findViewById(R.id.hiding)

        init {
            if (listener != null) {
                itemView.setOnClickListener { listener(boundItem, ACTION_DEFAULT, null) }
            }
        }

        override fun onBind(item: ViewModel) {
            lineView.text = item.line

            if (item.hidden) {
                hidingView.visibility = View.VISIBLE
                lineView.visibility = View.INVISIBLE
            } else {
                hidingView.visibility = View.GONE
                lineView.visibility = View.VISIBLE
            }
        }

    }

    // endregion

    companion object {
        @JvmStatic
        fun instantiateViewHolder(inflater: LayoutInflater,
                                  parent: ViewGroup,
                                  listener: ItemListener?)
                : ViewHolder {
            val view = inflater.inflate(R.layout.item_dialog, parent, false)
            return ViewHolder(view, listener)
        }
    }
}
