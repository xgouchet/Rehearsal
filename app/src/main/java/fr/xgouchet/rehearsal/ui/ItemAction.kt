package fr.xgouchet.rehearsal.ui

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import fr.xgouchet.rehearsal.R

class ItemAction {

    // region VM

    class ViewModel(
            id: Long,
            val direction: String = "",
            val hidden: Boolean = false,
            val colorIndex: Int = 0,
            val highlight: Boolean = false,
            data: Any? = null
    ) : Item.ViewModel(Item.Type.ACTION, id, data)

    // endregion

    // region VH

    class ViewHolder(
            itemView: View,
            listener: ItemListener?
    ) : Item.ViewHolder<ViewModel>(itemView) {

        private val directionView: TextView = itemView.findViewById(R.id.direction)
        private val hidingView: View = itemView.findViewById(R.id.hiding)
        private val highlightView: ImageView = itemView.findViewById(R.id.highlight)

        init {
            if (listener != null) {
                itemView.setOnClickListener { listener(boundItem, ACTION_DEFAULT, null) }
            }
        }

        override fun onBind(item: ViewModel) {
            directionView.text = item.direction

            val color = CharacterColor.get(hidingView.context, item.colorIndex)

            if (item.hidden) {
                hidingView.backgroundTintList = ColorStateList.valueOf(color)
                hidingView.visibility = View.VISIBLE
                directionView.visibility = View.INVISIBLE
            } else {
                hidingView.visibility = View.GONE
                directionView.visibility = View.VISIBLE
            }

            if (item.highlight) {
                highlightView.imageTintList = ColorStateList.valueOf(color)
                highlightView.visibility = View.VISIBLE
            } else {
                highlightView.visibility = View.GONE
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
            val view = inflater.inflate(R.layout.item_action, parent, false)
            return ViewHolder(view, listener)
        }
    }
}
