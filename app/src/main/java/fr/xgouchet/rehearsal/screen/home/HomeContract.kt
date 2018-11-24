package fr.xgouchet.rehearsal.screen.home

import fr.xgouchet.archx.ArchXPresenter
import fr.xgouchet.archx.ArchXView
import fr.xgouchet.archx.transformer.ArchXViewModelTransformer
import fr.xgouchet.rehearsal.core.model.Script
import fr.xgouchet.rehearsal.ui.Item

interface HomeContract {

    interface Presenter : ArchXPresenter<List<Item.ViewModel>> {
        fun onItemSelected(item: Item.ViewModel)
    }

    interface View : ArchXView<List<Item.ViewModel>> {
        fun navigateToScript(script: Script)
        fun showError(throwable: Throwable)
    }

    interface Transformer : ArchXViewModelTransformer<List<Script>, List<Item.ViewModel>>

}
