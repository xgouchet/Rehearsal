package fr.xgouchet.rehearsal.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import fr.xgouchet.rehearsal.R

class ItemCharacter {

    // region VM

    class ViewModel(
            val characterName: String = "",
            val characterExtension: String? = null,
            data: Any? = null
    ) : Item.ViewModel(Item.Type.CHARACTER, data)

    // endregion

    // region VH

    class ViewHolder(
            itemView: View,
            listener: ((Any) -> Unit)?
    ) : Item.ViewHolder<ViewModel>(itemView) {

        private val nameView: TextView = itemView.findViewById(R.id.character_name)
        private val extensionView: TextView = itemView.findViewById(R.id.character_extension)

        init {
            if (listener != null) {
                itemView.setOnClickListener { listener(boundItem) }
            }
        }

        @SuppressLint("SetTextI18n")
        override fun onBind(item: ViewModel) {

            nameView.text = item.characterName
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
                                  listener: ((Any) -> Unit)?)
                : ViewHolder {
            val view = inflater.inflate(R.layout.item_character, parent, false)
            return ViewHolder(view, listener)
        }
    }
}
