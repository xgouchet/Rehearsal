package fr.xgouchet.rehearsal.screen.props

import fr.xgouchet.archx.ArchXPresenter
import fr.xgouchet.archx.ArchXView
import fr.xgouchet.archx.transformer.ArchXViewModelTransformer
import fr.xgouchet.rehearsal.core.model.Prop
import fr.xgouchet.rehearsal.ui.Item

interface PropsContract {

    interface Presenter : ArchXPresenter<List<Item.ViewModel>> {
    }

    interface View : ArchXView<List<Item.ViewModel>> {
        fun showError(throwable: Throwable)
    }

    interface Transformer : ArchXViewModelTransformer<List<Prop>, List<Item.ViewModel>>


}
