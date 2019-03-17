package fr.xgouchet.rehearsal.ui

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import fr.xgouchet.rehearsal.R

class ItemAction {

    // region VM

    data class ViewModel(
            val id: Long,
            val direction: String = "",
            val hidden: Boolean = false,
            @ColorInt val color: Int = -1,
            val hasBookmark: Boolean = false,
            val hasNote: Boolean = false,
            val hasProps: Boolean = false,
            val data: Any? = null
    ) : Item.ViewModel() {
        override fun getItemType() = Item.Type.ACTION

        override fun getItemStableId() = id

        override fun getItemData(): Any? = data
    }

    // endregion

    // region VH

    class ViewHolder(
            itemView: View,
            listener: ItemListener
    ) : Item.ViewHolder<ViewModel>(itemView) {

        private val directionView: TextView = itemView.findViewById(R.id.direction)
        private val hidingView: View = itemView.findViewById(R.id.hiding)
        private val bookmarkView: ImageView = itemView.findViewById(R.id.bookmark)
        private val noteView: ImageView = itemView.findViewById(R.id.note)
        private val propView: ImageView = itemView.findViewById(R.id.prop)

        init {
            itemView.setOnLongClickListener { listener(boundItem, ACTION_LONG_CLICK, null) }
            itemView.setOnClickListener { listener(boundItem, ACTION_DEFAULT, null) }
            noteView.setOnClickListener { listener(boundItem, ACTION_NOTE, null) }
            propView.setOnClickListener { listener(boundItem, ACTION_PROP, null) }
        }

        override fun onBind(item: ViewModel) {
            directionView.text = MarkdownConverter.parse(item.direction)

            if (item.hidden) {
                hidingView.backgroundTintList = ColorStateList.valueOf(item.color)
                hidingView.visibility = View.VISIBLE
                directionView.visibility = View.INVISIBLE
            } else {
                hidingView.visibility = View.GONE
                directionView.visibility = View.VISIBLE
            }

            if (item.hasBookmark) {
                bookmarkView.imageTintList = ColorStateList.valueOf(item.color)
                bookmarkView.visibility = View.VISIBLE
            } else {
                bookmarkView.visibility = View.GONE
            }

            if (item.hasNote) {
                noteView.imageTintList = ColorStateList.valueOf(item.color)
                noteView.visibility = View.VISIBLE
            } else {
                noteView.visibility = View.GONE
            }

            if (item.hasProps) {
                propView.imageTintList = ColorStateList.valueOf(item.color)
                propView.visibility = View.VISIBLE
            } else {
                propView.visibility = View.GONE
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
            val view = inflater.inflate(R.layout.item_action, parent, false)
            return ViewHolder(view, listener)
        }
    }
}
