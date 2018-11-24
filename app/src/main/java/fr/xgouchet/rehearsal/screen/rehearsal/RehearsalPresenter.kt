package fr.xgouchet.rehearsal.screen.rehearsal

import fr.xgouchet.archx.data.ArchXDataPresenter
import fr.xgouchet.archx.data.ArchXDataSink
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.archx.rx.SchedulerProvider
import fr.xgouchet.archx.rx.schedule
import fr.xgouchet.archx.transformer.ArchXViewModelTransformer
import fr.xgouchet.rehearsal.core.model.Range
import fr.xgouchet.rehearsal.core.model.Rehearsal
import fr.xgouchet.rehearsal.ui.Item
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

class RehearsalPresenter(
        private val rehearsal: Rehearsal,
        dataSource: ArchXDataSource<List<Range>>,
        private val rehearsalDataSink: ArchXDataSink<Rehearsal>,
        private val rangeDataSink: ArchXDataSink<Range>,
        transformer: ArchXViewModelTransformer<List<Range>, List<Item.ViewModel>>,
        schedulerProvider: SchedulerProvider
) : ArchXDataPresenter<List<Range>, RehearsalContract.View, List<Item.ViewModel>>(dataSource, transformer, schedulerProvider),
        RehearsalContract.Presenter {

    private var deleteDisposable: Disposable? = null

    private var editRangeCompositeDisposable = CompositeDisposable()

    // region ArchXPresenter

    override fun onViewDetached() {
        super.onViewDetached()
        deleteDisposable?.dispose()
        deleteDisposable = null
    }

    // endregion

    // region ScheduleRangesContract.Presenter

    override fun onItemSelected(item: Item.ViewModel) {
        val range = item.getItemData() as? Range ?: return
        val scene = range.scene

        val start = range.fromCue
        val end = range.toCue

        view?.navigateToSceneWithRange(scene, start.position to end.position)
    }

    override fun onDeleteActionSelected() {
        deleteDisposable?.dispose()
        deleteDisposable = rehearsalDataSink.deleteData(rehearsal)
                .schedule(schedulerProvider)
                .subscribe(
                        { view?.navigateBack() },
                        { view?.showError(it) }
                )
    }

    override fun onAddRangeClicked() {
        view?.showRangePicker(rehearsal)
    }

    override fun onRangeCreated(range: Range) {
        val disposable = rangeDataSink.createData(range)
                .schedule(schedulerProvider)
                .subscribe(
                        { Timber.i("#created @range:$it") },
                        { view?.showError(it) }
                )
        editRangeCompositeDisposable.add(disposable)
    }
    // endregion
}
