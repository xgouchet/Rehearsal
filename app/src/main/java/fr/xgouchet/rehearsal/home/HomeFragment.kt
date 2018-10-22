package fr.xgouchet.rehearsal.home

import fr.xgouchet.archx.list.ArchXListFragment
import fr.xgouchet.rehearsal.core.ui.script.ScriptAdapter
import fr.xgouchet.rehearsal.core.ui.script.ScriptViewModel

class HomeFragment
    : ArchXListFragment<ScriptViewModel, ScriptAdapter>(),
        HomeContract.View {

    // region ArchXListFragment

    override val hasPagination: Boolean = false

    override fun createAdapter(): ScriptAdapter {
        return ScriptAdapter()
    }

    override fun showData(viewModel: List<ScriptViewModel>) {
        adapter.updateData(viewModel)
    }

    // endregion

}