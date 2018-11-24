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
            val scene: String = "",
            val startCharacter: String = "",
            val startLine: String = "",
            val endCharacter: String = "",
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

        private val titleView: TextView = itemView.findViewById(R.id.title)
        private val startNameView: TextView = itemView.findViewById(R.id.start_name)
        private val startLineView: TextView = itemView.findViewById(R.id.start_line)
        private val endNameView: TextView = itemView.findViewById(R.id.end_name)
        private val endLineView: TextView = itemView.findViewById(R.id.end_line)

        init {
            itemView.setOnClickListener { listener(boundItem, ACTION_DEFAULT, null) }
        }

        override fun onBind(item: ViewModel) {
            titleView.text = MarkdownConverter.parse(item.scene)
            startNameView.text = item.startCharacter
            startLineView.text = MarkdownConverter.parse(item.startLine)
            endNameView.text = item.endCharacter
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
