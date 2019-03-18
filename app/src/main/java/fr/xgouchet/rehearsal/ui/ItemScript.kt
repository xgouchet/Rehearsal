package fr.xgouchet.rehearsal.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import fr.xgouchet.rehearsal.R

class ItemScript {

    // region VM

    class ViewModel(
            val id: Long,
            val title: String = "",
            val author: String = "",
            val data: Any? = null
    ) : Item.ViewModel() {
        override fun getItemType() = Item.Type.SCRIPT

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
        private val authorView: TextView = itemView.findViewById(R.id.author)
        private val bookView: View = itemView.findViewById(R.id.book)

        init {
            itemView.setOnClickListener { listener(boundItem, ACTION_DEFAULT, null) }
        }

        @SuppressLint("SetTextI18n")
        override fun onBind(item: ViewModel) {

            titleView.text = MarkdownConverter.parse(item.title)
            authorView.text = MarkdownConverter.parse(item.author)

            val hashCode = item.author.hashCode()
            val modulo = hashCode % covers.size
            val coverIndex = if (modulo < 0) covers.size + modulo else modulo

            bookView.setBackgroundResource(covers[coverIndex])
        }

    }

    // endregion

    companion object {
        @JvmStatic
        fun instantiateViewHolder(inflater: LayoutInflater,
                                  parent: ViewGroup,
                                  listener: ItemListener)
                : ViewHolder {
            val view = inflater.inflate(R.layout.item_script, parent, false)
            return ViewHolder(view, listener)
        }

        val covers = listOf(
                R.drawable.bg_book_gradient_red,
                R.drawable.bg_book_gradient_green,
                R.drawable.bg_book_gradient_blue,
                R.drawable.bg_book_gradient_yellow,
                R.drawable.bg_book_gradient_purple,
                R.drawable.bg_book_gradient_grey
        )
    }
}
