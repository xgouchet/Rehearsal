package fr.xgouchet.rehearsal.scene

import fr.xgouchet.rehearsal.ui.ItemListFragment

class SceneFragment
    : ItemListFragment(),
        SceneContract.View {

    // region ItemListFragment

    override fun onItemAction(item: Any, action: String, value: String?) {
        (presenter as? SceneContract.Presenter)?.onItemSelected(item)
    }

    // endregion
}
