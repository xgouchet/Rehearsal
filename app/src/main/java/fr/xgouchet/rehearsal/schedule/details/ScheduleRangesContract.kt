package fr.xgouchet.rehearsal.schedule.details

import fr.xgouchet.archx.ArchXPresenter
import fr.xgouchet.archx.ArchXView
import fr.xgouchet.archx.ArchXViewModelTransformer
import fr.xgouchet.archx.data.ArchXDataSink
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.rehearsal.core.room.model.RangeModel
import fr.xgouchet.rehearsal.core.room.model.SceneModel
import fr.xgouchet.rehearsal.core.room.model.ScheduleModel
import fr.xgouchet.rehearsal.ui.Item

interface ScheduleRangesContract {

    interface Presenter : ArchXPresenter<List<Item.ViewModel>> {
        fun onItemSelected(item: Item.ViewModel)
        fun onDeleteActionSelected()
    }

    interface View : ArchXView<List<Item.ViewModel>> {
        fun showError(throwable: Throwable)

        fun navigateToSceneWithRange(scene: SceneModel, range: Pair<Int, Int>)
        fun navigateBack()
    }

    interface DataSource : ArchXDataSource<List<RangeModel>>

    interface ScheduleDataSink : ArchXDataSink<ScheduleModel>

    interface Transformer : ArchXViewModelTransformer<List<RangeModel>, List<Item.ViewModel>>
}
