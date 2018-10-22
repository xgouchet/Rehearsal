package fr.xgouchet.rehearsal.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import fr.xgouchet.archx.ui.ArchXAdapter

class ItemAdapter(private val listener: ((Any) -> Unit)?)
    : ArchXAdapter<Item.ViewModel, Item.ViewHolder<*>>() {


    override fun getItemViewType(position: Int): Int {
        return (getItem(position)?.type ?: Item.Type.EMPTY).ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item.ViewHolder<*> {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {

            Item.Type.HEADER.ordinal -> ItemHeader.instantiateViewHolder(inflater, parent, listener)
            Item.Type.SCRIPT.ordinal -> ItemScript.instantiateViewHolder(inflater, parent, listener)
            Item.Type.EMPTY.ordinal -> ItemEmpty.instantiateViewHolder(inflater, parent,listener)

            else -> TODO()
        }
    }
}
