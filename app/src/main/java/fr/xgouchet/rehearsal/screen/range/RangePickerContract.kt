package fr.xgouchet.rehearsal.screen.range

import fr.xgouchet.archx.ArchXPresenter
import fr.xgouchet.archx.ArchXView
import fr.xgouchet.rehearsal.core.model.Range
import fr.xgouchet.rehearsal.ui.Item

interface RangePickerContract {

    interface Presenter : ArchXPresenter<List<Item.ViewModel>> {
        fun onItemSelected(item: Item.ViewModel)

        fun onScenePicked(sceneId: Long)
        fun onCuePicked(requestId: Int, cueId: Long)
        fun onValidate()
    }

    interface View : ArchXView<List<Item.ViewModel>> {
        fun showScenePicker(scenes: List<Pair<Long, String>>)
        fun showCuePicker(requestId: Int, cues: List<Pair<Long, String>>)
        fun showValidate(valid: Boolean)
        fun showError(throwable: Throwable)

        fun navigateBackWithResult(range: Range)
    }

}
