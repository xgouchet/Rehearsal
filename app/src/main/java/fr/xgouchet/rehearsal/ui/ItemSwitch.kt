package fr.xgouchet.rehearsal.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.widget.SwitchCompat
import fr.xgouchet.rehearsal.R

class ItemSwitch {

    // region VM

    class ViewModel(
            val id: Long,
            val label: String = "",
            @StringRes val labelRes: Int = 0,
            val value: Boolean = false,
            val data: Any? = null
    ) : Item.ViewModel() {
        override fun getItemType() = Item.Type.SWITCH

        override fun getItemStableId() = id

        override fun getItemData(): Any? = data
    }

    // endregion

    // region VH

    class ViewHolder(
            itemView: View,
            private val listener: ItemListener?
    ) : Item.ViewHolder<ViewModel>(itemView) {

        private val switchView: SwitchCompat = itemView.findViewById(R.id.switch_input)

        init {

        }

        @SuppressLint("SetTextI18n")
        override fun onBind(item: ViewModel) {

            if (item.label.isNotBlank()) {
                switchView.text = item.label
            } else if (item.labelRes != 0) {
                switchView.setText(item.labelRes)
            }

            switchView.setOnCheckedChangeListener(null)

            switchView.isChecked = item.value
//            switchView.isEnabled = !item.isReadOnly
            listener?.let { l ->
                switchView.setOnCheckedChangeListener { _, value ->
                    l(boundItem, ACTION_VALUE_CHANGED, value.toString())
                }
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
            val view = inflater.inflate(R.layout.item_switch, parent, false)
            return ViewHolder(view, listener)
        }

    }
}
