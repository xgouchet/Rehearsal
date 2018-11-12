package fr.xgouchet.rehearsal.ui

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import fr.xgouchet.rehearsal.R

class ItemColorPicker {

    // region VM

    data class ViewModel(
            val id: Long,
            val label: String = "",
            @StringRes val labelRes: Int = 0,
            @ColorInt val color: Int = 0,
            val data: Any? = null
    ) : Item.ViewModel() {
        override fun getItemType() = Item.Type.COLOR

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
        private val colorView: ImageView = itemView.findViewById(R.id.color)

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

            colorView.imageTintList = ColorStateList.valueOf(item.color)
        }

    }

    // endregion

    companion object {
        @JvmStatic
        fun instantiateViewHolder(inflater: LayoutInflater,
                                  parent: ViewGroup,
                                  listener: ItemListener)
                : ViewHolder {
            val view = inflater.inflate(R.layout.item_color, parent, false)
            return ViewHolder(view, listener)
        }

    }
}
