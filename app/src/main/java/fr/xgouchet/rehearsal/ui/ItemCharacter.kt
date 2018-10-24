package fr.xgouchet.rehearsal.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import fr.xgouchet.rehearsal.R

class ItemCharacter {

    // region VM

    class ViewModel(
            val characterName: String = "",
            val characterExtension: String? = null,
            val colorIndex: Int = 0,
            data: Any? = null
    ) : Item.ViewModel(Item.Type.CHARACTER, data)

    // endregion

    // region VH

    class ViewHolder(
            itemView: View,
            listener: ItemListener?
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

            val colorIdx = item.colorIndex % characterColors.size
            val colorRes = characterColors[colorIdx]
            val color = ContextCompat.getColor(nameView.context, colorRes)
            nameView.setTextColor(color)

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
                                  listener: ItemListener?)
                : ViewHolder {
            val view = inflater.inflate(R.layout.item_character, parent, false)
            return ViewHolder(view, listener)
        }

        private val characterColors = listOf(
                R.color.character_fg_0,
                R.color.character_fg_1,
                R.color.character_fg_2,
                R.color.character_fg_3,
                R.color.character_fg_4,
                R.color.character_fg_5,
                R.color.character_fg_6,
                R.color.character_fg_7,
                R.color.character_fg_8,
                R.color.character_fg_9,
                R.color.character_fg_10,
                R.color.character_fg_11,
                R.color.character_fg_12,
                R.color.character_fg_13,
                R.color.character_fg_14,
                R.color.character_fg_15
        )
    }
}
