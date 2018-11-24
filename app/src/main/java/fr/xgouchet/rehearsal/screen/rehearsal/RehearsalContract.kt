package fr.xgouchet.rehearsal.screen.rehearsal

import fr.xgouchet.archx.ArchXPresenter
import fr.xgouchet.archx.ArchXView
import fr.xgouchet.archx.transformer.ArchXViewModelTransformer
import fr.xgouchet.rehearsal.core.model.Range
import fr.xgouchet.rehearsal.core.model.Rehearsal
import fr.xgouchet.rehearsal.core.model.Scene
import fr.xgouchet.rehearsal.ui.Item

interface RehearsalContract {

    interface Presenter : ArchXPresenter<List<Item.ViewModel>> {
        fun onItemSelected(item: Item.ViewModel)
        fun onDeleteActionSelected()
        fun onAddRangeClicked()
        fun onRangeCreated(range: Range)
    }

    interface View : ArchXView<List<Item.ViewModel>> {
        fun showError(throwable: Throwable)
        fun showRangePicker(rehearsal: Rehearsal)

        fun navigateToSceneWithRange(scene: Scene, range: Pair<Int, Int>)
        fun navigateBack()
    }

    interface Transformer : ArchXViewModelTransformer<List<Range>, List<Item.ViewModel>>
}
