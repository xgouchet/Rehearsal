package fr.xgouchet.rehearsal.schedule.details

import androidx.lifecycle.LifecycleOwner
import fr.xgouchet.archx.ArchXViewModelTransformer
import fr.xgouchet.archx.data.ArchXDataPresenter
import fr.xgouchet.archx.data.ArchXDataSink
import fr.xgouchet.rehearsal.core.room.model.RangeModel
import fr.xgouchet.rehearsal.ui.Item

class ScheduleRangesPresenter(
        owner: LifecycleOwner,
        dataSource: ScheduleRangesContract.DataSource,
        dataSink: ArchXDataSink<List<RangeModel>>,
        transformer: ArchXViewModelTransformer<List<RangeModel>, List<Item.ViewModel>>
) : ArchXDataPresenter<List<RangeModel>, ScheduleRangesContract.View, List<Item.ViewModel>>(owner, dataSource, dataSink, transformer),
        ScheduleRangesContract.Presenter {

    // region ScheduleRangesContract.Presenter

    override fun onItemSelected(item: Item.ViewModel) {
        val range = item.getItemData() as? RangeModel ?: return
        val scene = range.scene ?: return

        val start = range.startCue ?: return
        val end = range.endCue ?: return

        view?.navigateToSceneWithRange(scene, start.position to end.position)
    }

    // endregion
}
