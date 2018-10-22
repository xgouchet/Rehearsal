package fr.xgouchet.rehearsal.home

import fr.xgouchet.rehearsal.ui.ItemListFragment

class HomeFragment
    : ItemListFragment(),
        HomeContract.View {

    override fun onItemSelected(item: Any) {
        (presenter as? HomeContract.Presenter)?.onItemSelected(item)
    }
}
