package fr.xgouchet.rehearsal.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import fr.xgouchet.rehearsal.R

class ItemCharacter {

    // region VM

    data class ViewModel(
            val id: Long,
            val characterName: String = "",
            val characterExtension: String? = null,
            @ColorInt val color: Int = -1,
            val data: Any? = null
    ) : Item.ViewModel() {
        override fun getItemType() = Item.Type.CHARACTER

        override fun getItemStableId() = id

        override fun getItemData(): Any? = data
    }

    // endregion

    // region VH

    class ViewHolder(
            itemView: View,
            listener: ItemListener
    ) : Item.ViewHolder<ViewModel>(itemView) {

        private val nameView: TextView = itemView.findViewById(R.id.character_name)
        private val extensionView: TextView = itemView.findViewById(R.id.character_extension)

        init {
            if (listener != null) {
                itemView.setOnClickListener { listener(boundItem, ACTION_DEFAULT, null) }
            }
        }

        @SuppressLint("SetTextI18n")
        override fun onBind(item: ViewModel) {

            nameView.text = item.characterName

            nameView.setTextColor(item.color)

            if (item.characterExtension.isNullOrBlank()) {
                extensionView.visibility = View.GONE
            } else {
                extensionView.visibility = View.VISIBLE
                extensionView.text = item.characterExtension
            }
        }

    }

    // endregion

    companion object {
        @JvmStatic
        fun instantiateViewHolder(inflater: LayoutInflater,
                                  parent: ViewGroup,
                                  listener: ItemListener)
                : ViewHolder {
            val view = inflater.inflate(R.layout.item_character, parent, false)
            return ViewHolder(view, listener)
        }

    }
}
