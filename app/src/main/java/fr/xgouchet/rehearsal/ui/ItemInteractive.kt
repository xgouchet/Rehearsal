package fr.xgouchet.rehearsal.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import fr.xgouchet.rehearsal.R

class ItemInteractive {

    // region VM

    data class ViewModel(
            val id: Long,
            val label: String = "",
            @StringRes val labelRes: Int = 0,
            val value: String = "",
            val data: Any? = null
    ) : Item.ViewModel() {
        override fun getItemType() = Item.Type.INTERACTIVE

        override fun getItemStableId() = id

        override fun getItemData(): Any? = data
    }


    // endregion

    // region VH

    class ViewHolder(
            itemView: View,
            listener: ItemListener
    ) : Item.ViewHolder<ViewModel>(itemView) {

        private val labelView: TextView = itemView.findViewById(R.id.label)
        private val valueView: TextView = itemView.findViewById(R.id.value)

        init {
            listener?.let { l ->
                itemView.setOnClickListener { l(boundItem, ACTION_DEFAULT, null) }
            }
        }

        @SuppressLint("SetTextI18n")
        override fun onBind(item: ViewModel) {

            if (item.label.isNotBlank()) {
                labelView.text = item.label
            } else if (item.labelRes != 0) {
                labelView.setText(item.labelRes)
            }

            valueView.text = item.value
        }

    }

    // endregion

    companion object {
        @JvmStatic
        fun instantiateViewHolder(inflater: LayoutInflater,
                                  parent: ViewGroup,
                                  listener: ItemListener)
                : ViewHolder {
            val view = inflater.inflate(R.layout.item_interactive, parent, false)
            return ViewHolder(view, listener)
        }

    }
}
