package fr.xgouchet.rehearsal.ui

import android.view.View
import fr.xgouchet.archx.ui.ArchXViewHolder

class Item {

    open class ViewModel(val type: Type,
                         val data: Any? = null)

    abstract class ViewHolder<VM : ViewModel>(itemView: View)
        : ArchXViewHolder<ViewModel>(itemView) {

        protected lateinit var boundItem: VM

        @Suppress("UNCHECKED_CAST")
        final override fun bind(item: ViewModel) {
            val cast = item as? VM ?: return
            boundItem = cast
            onBind(cast)
        }

        abstract fun onBind(item: VM)
    }


    enum class Type {
        EMPTY,
        SCRIPT,
        SCENE,
        CHARACTER,
        DIALOG,
        ACTION,
        DIVIDER
    }
}
