package fr.xgouchet.rehearsal.scene

import fr.xgouchet.rehearsal.ui.ItemListFragment

class SceneFragment
    : ItemListFragment(),
        SceneContract.View {

    override fun onItemSelected(item: Any) {
        (presenter as? SceneContract.Presenter)?.onItemSelected(item)
    }
}
