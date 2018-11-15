package fr.xgouchet.rehearsal.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import fr.xgouchet.rehearsal.R

class ItemRange {

    // region VM

    data class ViewModel(
            val id: Long,
            val startLine: String = "",
            val endLine: String = "",
            val data: Any? = null
    ) : Item.ViewModel() {
        override fun getItemType() = Item.Type.RANGE

        override fun getItemStableId() = id

        override fun getItemData(): Any? = data
    }


    // endregion

    // region VH

    class ViewHolder(
            itemView: View,
            listener: ItemListener
    ) : Item.ViewHolder<ViewModel>(itemView) {

        private val startLineView: TextView = itemView.findViewById(R.id.start_line)
        private val endLineView: TextView = itemView.findViewById(R.id.end_line)

        init {
//            itemView.setOnLongClickListener { listener(boundItem, ACTION_LONG_CLICK, null) }
//            itemView.setOnClickListener { listener(boundItem, ACTION_DEFAULT, null) }
//            noteView.setOnClickListener { listener(boundItem, ACTION_NOTE, null) }
        }

        override fun onBind(item: ViewModel) {
            startLineView.text = MarkdownConverter.parse(item.startLine)
            endLineView.text = MarkdownConverter.parse(item.endLine)
        }

    }

    // endregion

    companion object {
        @JvmStatic
        fun instantiateViewHolder(inflater: LayoutInflater,
                                  parent: ViewGroup,
                                  listener: ItemListener)
                : ViewHolder {
            val view = inflater.inflate(R.layout.item_range, parent, false)
            return ViewHolder(view, listener)
        }
    }
}
