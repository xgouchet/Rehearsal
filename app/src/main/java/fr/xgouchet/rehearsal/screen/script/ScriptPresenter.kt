package fr.xgouchet.rehearsal.screen.script

import android.content.Context
import android.net.Uri
import android.widget.Toast
import fr.xgouchet.archx.data.ArchXDataPresenter
import fr.xgouchet.archx.data.ArchXDataSink
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.archx.rx.SchedulerProvider
import fr.xgouchet.archx.rx.schedule
import fr.xgouchet.rehearsal.core.model.Scene
import fr.xgouchet.rehearsal.core.model.Script
import fr.xgouchet.rehearsal.ui.Item
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.text.Normalizer

class ScriptPresenter(
        private val script: Script,
        private val context : Context,
        dataSource: ArchXDataSource<List<Scene>>,
        private val scriptDataSink: ArchXDataSink<Script>,
        transformer: ScriptContract.Transformer,
        schedulerProvider: SchedulerProvider
) : ArchXDataPresenter<List<Scene>, ScriptContract.View, List<Item.ViewModel>>(dataSource, transformer, schedulerProvider),
        ScriptContract.Presenter {

    private var showEmptyScenes = true
    private var rawData: List<Scene> = emptyList()

    private var exportDisposable: Disposable? = null
    private var deletingDisposable: Disposable? = null

    // region ArchXPresenter

    override fun onViewDetached() {
        super.onViewDetached()
        deletingDisposable?.dispose()
        exportDisposable?.dispose()
        deletingDisposable = null
        exportDisposable = null
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

    override fun onExportScript() {
        val normalizedTitle = Normalizer.normalize(script.title, Normalizer.Form.NFKD)
        val simplifiedTitle = Regex("\\p{InCombiningDiacriticalMarks}+").replace(normalizedTitle, "")
        val filename = Regex("[^a-zA-Z0-9]").replace(simplifiedTitle, "_")
        view?.requestDocumentUri("$filename.fountain")
    }


    override fun onExportScriptToUri(uri: Uri) {
        exportDisposable = Single.create(ExportFountainDocument(context, script, uri))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            Toast.makeText(context, "Export successful", Toast.LENGTH_LONG).show()
                            Timber.i("#export @result:$it")
                        },
                        {
                            Toast.makeText(context, "Export failed", Toast.LENGTH_LONG).show()
                            Timber.i(it, "#error #export")
                        }
                )
    }

    // endregion

}
