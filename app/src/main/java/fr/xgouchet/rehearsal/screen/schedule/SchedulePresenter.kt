package fr.xgouchet.rehearsal.screen.schedule

import fr.xgouchet.archx.data.ArchXDataPresenter
import fr.xgouchet.archx.data.ArchXDataSink
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.archx.rx.SchedulerProvider
import fr.xgouchet.archx.rx.schedule
import fr.xgouchet.rehearsal.core.model.Rehearsal
import fr.xgouchet.rehearsal.core.model.Script
import fr.xgouchet.rehearsal.ui.Item
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.util.Date
import java.util.concurrent.TimeUnit

class SchedulePresenter(
        private val script: Script,
        dataSource: ArchXDataSource<List<Rehearsal>>,
        private val dataSink: ArchXDataSink<Rehearsal>,
        transformer: ScheduleContract.Transformer,
        schedulerProvider: SchedulerProvider
) : ArchXDataPresenter<List<Rehearsal>, ScheduleContract.View, List<Item.ViewModel>>(dataSource, transformer, schedulerProvider),
        ScheduleContract.Presenter {

    private val creationCompositeDisposable = CompositeDisposable()

    // region ArchXPresenter

    override fun onViewDetached() {
        super.onViewDetached()
        creationCompositeDisposable.clear()
    }

    //endregion

    // region ScheduleContract.Presenter

    override fun onItemSelected(item: Item.ViewModel) {
        val set = item.getItemData() as? Rehearsal ?: return
        view?.navigateToSetDetails(set)
    }

    override fun onAddSchedule() {
        val dueDate = Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7))
        view?.showDatePicker(dueDate)
    }

    override fun onDatePicked(date: Date) {
        val rehearsal = Rehearsal(
                rehearsalId = 0,
                scriptId = script.scriptId,
                dueDate = date
        )

        val disposable = dataSink.createData(rehearsal)
                .schedule(schedulerProvider)
                .subscribe(
                        { Timber.i("#rehearsal #created @rehearsal:$it") },
                        { view?.showError(it) }
                )
        creationCompositeDisposable.add(disposable)
    }

    // endregion

}
