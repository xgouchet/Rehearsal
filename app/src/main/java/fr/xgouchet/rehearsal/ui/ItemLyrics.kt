package fr.xgouchet.rehearsal.ui

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import fr.xgouchet.rehearsal.R

class ItemLyrics {

    // region VM

    class ViewModel(
            id: Long,
            val lyrics: String = "",
            val hidden: Boolean = false,
            val colorIndex: Int = 0,
            val highlight: Boolean = false,
            data: Any? = null
    ) : Item.ViewModel(Item.Type.LYRICS, id, data)

    // endregion

    // region VH

    class ViewHolder(
            itemView: View,
            listener: ItemListener?
    ) : Item.ViewHolder<ViewModel>(itemView) {

        private val lyricsView: TextView = itemView.findViewById(R.id.lyrics)
        private val hidingView: View = itemView.findViewById(R.id.hiding)
        private val highlightView: ImageView = itemView.findViewById(R.id.highlight)

        init {
            if (listener != null) {
                itemView.setOnClickListener { listener(boundItem, ACTION_DEFAULT, null) }
            }
        }

        override fun onBind(item: ViewModel) {
            lyricsView.text = item.lyrics

            val color = CharacterColor.get(hidingView.context, item.colorIndex)

            if (item.hidden) {
                hidingView.backgroundTintList = ColorStateList.valueOf(color)
                hidingView.visibility = View.VISIBLE
                lyricsView.visibility = View.INVISIBLE
            } else {
                hidingView.visibility = View.GONE
                lyricsView.visibility = View.VISIBLE
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
            val view = inflater.inflate(R.layout.item_lyrics, parent, false)
            return ViewHolder(view, listener)
        }
    }
}
