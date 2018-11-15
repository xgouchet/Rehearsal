package fr.xgouchet.rehearsal.range

import fr.xgouchet.archx.ArchXPresenter
import fr.xgouchet.archx.ArchXView
import fr.xgouchet.archx.ArchXViewModelTransformer
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.rehearsal.core.room.join.CueWithCharacter
import fr.xgouchet.rehearsal.core.room.model.RangeModel
import fr.xgouchet.rehearsal.core.room.model.SceneModel
import fr.xgouchet.rehearsal.core.room.model.ScheduleModel
import fr.xgouchet.rehearsal.ui.Item

interface RangePickerContract {

    interface Presenter : ArchXPresenter<List<Item.ViewModel>> {
        fun onItemSelected(item: Item.ViewModel)
        fun onItemValueChanged(item: Item.ViewModel, value: String?)

        fun onScenePicked(sceneId: Int)
        fun onCuePicked(requestId: Int, cueId: Int)
        fun onValidate()
    }

    interface View : ArchXView<List<Item.ViewModel>> {
        fun showScenePicker(scenes: List<Pair<Int, String>>)
        fun showCuePicker(requestId: Int, cues: List<Pair<Int, String>>)
        fun showValidate(valid: Boolean)

        fun navigateBackWithResult(rangeModel: RangeModel)
    }

    interface SceneDataSource : ArchXDataSource<List<SceneModel>>
    interface CueDataSource : ArchXDataSource<List<CueWithCharacter>>

    interface Transformer : ArchXViewModelTransformer<List<ScheduleModel>, List<Item.ViewModel>>
}
