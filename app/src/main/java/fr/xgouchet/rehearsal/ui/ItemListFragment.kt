package fr.xgouchet.rehearsal.ui

import fr.xgouchet.archx.list.ArchXListFragment

abstract class ItemListFragment
    : ArchXListFragment<Item.ViewModel, ItemAdapter>() {

    // region ArchXListFragment

    override fun createAdapter(): ItemAdapter {
        return ItemAdapter {
            onItemSelected(it)
        }
    }

    override fun showData(viewModel: List<Item.ViewModel>) {
        adapter.updateData(viewModel)
    }

    // endregion

    protected abstract fun onItemSelected(item: Any)

}
