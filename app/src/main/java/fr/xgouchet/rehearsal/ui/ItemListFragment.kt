package fr.xgouchet.rehearsal.ui

import fr.xgouchet.archx.list.ArchXListFragment

abstract class ItemListFragment
    : ArchXListFragment<Item.ViewModel, ItemAdapter>() {

    // region ArchXListFragment

    override fun createAdapter(): ItemAdapter {
        return ItemAdapter { o, a, v ->
            onItemAction(o, a, v)
        }
    }

    override fun showData(viewModel: List<Item.ViewModel>) {
        adapter.updateData(viewModel)
    }

    // endregion

    protected abstract fun onItemAction(item: Any, action: String, value: String?)

}
