package fr.xgouchet.rehearsal.ui

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import fr.xgouchet.rehearsal.R

class ItemDialog {

    // region VM

    data class ViewModel(
            val id: Long,
            val line: String = "",
            val hidden: Boolean = false,
            @ColorInt val color: Int = -1,
            @ColorInt val highlightColor: Int? = null,
            val hasBookmark: Boolean = false,
            val hasNote: Boolean = false,
            val hasProps: Boolean = false,
            val data: Any? = null
    ) : Item.ViewModel() {
        override fun getItemType() = Item.Type.DIALOG

        override fun getItemStableId() = id

        override fun getItemData(): Any? = data
    }


    // endregion

    // region VH

    class ViewHolder(
            itemView: View,
            listener: ItemListener
    ) : Item.ViewHolder<ViewModel>(itemView) {

        private val lineView: TextView = itemView.findViewById(R.id.line)
        private val hidingView: View = itemView.findViewById(R.id.hiding)
        private val bookmarkView: ImageView = itemView.findViewById(R.id.bookmark)
        private val noteView: ImageView = itemView.findViewById(R.id.note)
        private val propView: ImageView = itemView.findViewById(R.id.prop)
        private val highlightView: View = itemView.findViewById(R.id.highlight)

        init {
            itemView.setOnLongClickListener { listener(boundItem, ACTION_LONG_CLICK, null) }
            itemView.setOnClickListener { listener(boundItem, ACTION_DEFAULT, null) }
            noteView.setOnClickListener { listener(boundItem, ACTION_NOTE, null) }
            propView.setOnClickListener { listener(boundItem, ACTION_PROP, null) }
        }

        override fun onBind(item: ViewModel) {
            lineView.text = MarkdownConverter.parse(item.line)

            if (item.hidden) {
                hidingView.backgroundTintList = ColorStateList.valueOf(item.color)
                hidingView.visibility = View.VISIBLE
                lineView.visibility = View.INVISIBLE
            } else {
                hidingView.visibility = View.GONE
                lineView.visibility = View.VISIBLE
            }

            if (item.highlightColor != null) {
                highlightView.backgroundTintList = ColorStateList.valueOf(item.highlightColor)
                highlightView.visibility = View.VISIBLE
            } else {
                highlightView.visibility = View.GONE
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
            val view = inflater.inflate(R.layout.item_dialog, parent, false)
            return ViewHolder(view, listener)
        }
    }
}
