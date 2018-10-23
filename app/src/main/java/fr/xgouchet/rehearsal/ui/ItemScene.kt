package fr.xgouchet.rehearsal.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import fr.xgouchet.rehearsal.R

class ItemScene {

    // region VM

    class ViewModel(
            val title: String = "",
            val numbering: String = "",
            data: Any? = null
    ) : Item.ViewModel(Item.Type.SCENE, data)

    // endregion

    // region VH

    class ViewHolder(
            itemView: View,
            listener: ((Any) -> Unit)?
    ) : Item.ViewHolder<ViewModel>(itemView) {

        private val titleView: TextView = itemView.findViewById(R.id.title)
        private val numberingView: TextView = itemView.findViewById(R.id.number)

        init {
            if (listener != null) {
                itemView.setOnClickListener { listener(boundItem) }
            }
        }

        @SuppressLint("SetTextI18n")
        override fun onBind(item: ViewModel) {

            titleView.text = item.title
            numberingView.text = item.numbering
        }

    }

    // endregion

    companion object {
        @JvmStatic
        fun instantiateViewHolder(inflater: LayoutInflater,
                                  parent: ViewGroup,
                                  listener: ((Any) -> Unit)?)
                : ViewHolder {
            val view = inflater.inflate(R.layout.item_scene, parent, false)
            return ViewHolder(view, listener)
        }
    }
}
