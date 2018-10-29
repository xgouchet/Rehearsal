package fr.xgouchet.rehearsal.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import fr.xgouchet.archx.ui.ArchXAdapter

class ItemAdapter(private val listener: ItemListener?)
    : ArchXAdapter<Item.ViewModel, Item.ViewHolder<*>, Long>(extractId = { it.id }) {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.id ?: 0L
    }

    override fun getItemViewType(position: Int): Int {
        return (getItem(position)?.type ?: Item.Type.EMPTY).ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item.ViewHolder<*> {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {

            Item.Type.SCRIPT.ordinal -> ItemScript.instantiateViewHolder(inflater, parent, listener)
            Item.Type.SCENE.ordinal -> ItemScene.instantiateViewHolder(inflater, parent, listener)
            Item.Type.CHARACTER.ordinal -> ItemCharacter.instantiateViewHolder(inflater, parent, listener)
            Item.Type.DIALOG.ordinal -> ItemDialog.instantiateViewHolder(inflater, parent, listener)
            Item.Type.ACTION.ordinal -> ItemAction.instantiateViewHolder(inflater, parent, listener)
            Item.Type.LYRICS.ordinal -> ItemLyrics.instantiateViewHolder(inflater, parent, listener)

            Item.Type.EMPTY.ordinal -> ItemEmpty.instantiateViewHolder(inflater, parent)
            Item.Type.DIVIDER.ordinal -> ItemDivider.instantiateViewHolder(inflater, parent)

            Item.Type.SWITCH.ordinal -> ItemSwitch.instantiateViewHolder(inflater, parent, listener)

            else -> TODO()
        }
    }
}
