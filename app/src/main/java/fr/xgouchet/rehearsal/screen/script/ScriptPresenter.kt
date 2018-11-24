package fr.xgouchet.rehearsal.screen.script

import fr.xgouchet.archx.data.ArchXDataPresenter
import fr.xgouchet.archx.data.ArchXDataSink
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.archx.rx.SchedulerProvider
import fr.xgouchet.archx.rx.schedule
import fr.xgouchet.rehearsal.core.model.Scene
import fr.xgouchet.rehearsal.core.model.Script
import fr.xgouchet.rehearsal.ui.Item
import io.reactivex.disposables.Disposable
import timber.log.Timber

class ScriptPresenter(
        private val script: Script,
        dataSource: ArchXDataSource<List<Scene>>,
        private val scriptDataSink: ArchXDataSink<Script>,
        transformer: ScriptContract.Transformer,
        schedulerProvider: SchedulerProvider
) : ArchXDataPresenter<List<Scene>, ScriptContract.View, List<Item.ViewModel>>(dataSource, transformer, schedulerProvider),
        ScriptContract.Presenter {

    private var showEmptyScenes = true
    private var rawData: List<Scene> = emptyList()


    private var deletingDisposable: Disposable? = null

    // region ArchXPresenter

    override fun onViewDetached() {
        super.onViewDetached()
        deletingDisposable?.dispose()
        deletingDisposable = null
    }

    // endregion

    // region ScriptContract.Presenter

    override fun onItemSelected(item: Item.ViewModel) {
        val scene = item.getItemData() as? Scene

        if (scene != null) {
            view?.navigateToScene(scene)
        }
    }

    override fun onScheduleActionSelected() {
        view?.navigateToSchedule(script)
    }

    override fun onCastActionSelected() {
        view?.navigateToCastSettings(script)
    }

    override fun onDeleteActionSelected() {
        deletingDisposable?.dispose()
        deletingDisposable = scriptDataSink.deleteData(script)
                .schedule(schedulerProvider)
                .subscribe(
                        { view?.navigateBack() },
                        { view?.showError(it) }
                )
    }

    override fun onHideEmptyScenes() {
        showEmptyScenes = false
        onDataChanged(rawData)
    }

    override fun onShowEmptyScenes() {
        showEmptyScenes = true
        onDataChanged(rawData)
    }

    override fun onDataChanged(t: List<Scene>) {
        rawData = t
        val filtered = if (showEmptyScenes) t else t.filter { it.cuesCount > 0 }
        val viewModel = transformer.transform(filtered)
        view?.showData(viewModel)
        view?.setEmptyScenesVisible(showEmptyScenes)
    }

    // endregion

}
