package fr.xgouchet.rehearsal.ui

import fr.xgouchet.archx.list.ArchXListFragment

open class ItemListFragment
    : ArchXListFragment<Item.ViewModel, ItemAdapter>() {

    // region ArchXListFragment

    override fun createAdapter(): ItemAdapter {
        return ItemAdapter()
    }

    override fun showData(viewModel: List<Item.ViewModel>) {
        adapter.updateData(viewModel)
    }

    // endregion
}
