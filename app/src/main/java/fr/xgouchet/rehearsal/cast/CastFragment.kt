package fr.xgouchet.rehearsal.cast

import fr.xgouchet.rehearsal.ui.ACTION_DEFAULT
import fr.xgouchet.rehearsal.ui.ACTION_VALUE_CHANGED
import fr.xgouchet.rehearsal.ui.ItemListFragment

class CastFragment
    : ItemListFragment(),
        CastContract.View {


    // region ItemListFragment

    override fun onItemAction(item: Any, action: String, value: String?) {
        when (action) {
            ACTION_DEFAULT -> {}
            ACTION_VALUE_CHANGED ->(presenter as? CastContract.Presenter)?.onItemValueChanged(item, value)
        }

    }

    // endregion
}
