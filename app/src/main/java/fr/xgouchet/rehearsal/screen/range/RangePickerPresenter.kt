package fr.xgouchet.rehearsal.screen.range

import fr.xgouchet.archx.ArchXView
import fr.xgouchet.archx.data.ArchXDataPresenter
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.archx.rx.SchedulerProvider
import fr.xgouchet.archx.rx.schedule
import fr.xgouchet.rehearsal.R
import fr.xgouchet.rehearsal.core.model.Cue
import fr.xgouchet.rehearsal.core.model.Range
import fr.xgouchet.rehearsal.core.model.Scene
import fr.xgouchet.rehearsal.core.room.model.CueDbModel
import fr.xgouchet.rehearsal.core.ui.PrincipledViewModelTransformer
import fr.xgouchet.rehearsal.ext.getAbstract
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemInteractive
import fr.xgouchet.rehearsal.ui.StableId
import io.reactivex.disposables.Disposable

class RangePickerPresenter(
        private val rehearsalId: Long,
        scenesDataSource: ArchXDataSource<List<Scene>>,
        private val cuesDataSourceProvider: (Long) -> ArchXDataSource<List<Cue>>,
        schedulerProvider: SchedulerProvider
) : ArchXDataPresenter<List<Scene>, RangePickerContract.View, List<Item.ViewModel>>(scenesDataSource, PrincipledViewModelTransformer(), schedulerProvider),
        RangePickerContract.Presenter {

    private var scenes: List<Scene> = emptyList()
    private var selectedScene: Scene? = null

    private var cues: List<Cue> = emptyList()

    private var selectedStart: Cue? = null
    private var selectedEnd: Cue? = null

    private var cuesDisposable: Disposable? = null

    // region ArchXContract.Presenter

    override fun onViewAttached(view: ArchXView<List<Item.ViewModel>>, isRestored: Boolean) {
        super.onViewAttached(view, isRestored)

        updateForm()
    }

    override fun onViewDetached() {
        super.onViewDetached()
        cuesDisposable?.dispose()
        cuesDisposable = null
    }

    override fun onDataChanged(t: List<Scene>) {
        scenes = t
        updateSceneSelection()
    }

    override fun onErrorLoadingData(it: Throwable) {
        view?.showError(it)
    }

    // endregion

    // region RangePickerContract.Presenter

    override fun onItemSelected(item: Item.ViewModel) {
        val subIndex = StableId.getSubIndex(item.getItemStableId())

        when (subIndex) {
            IDX_SCENE -> {
                view?.showScenePicker(
                        scenes
                                .filter { it.cuesCount > 0 }
                                .map { it.sceneId to it.description() })
            }
            IDX_FROM -> {
                val endPosition = selectedEnd?.position ?: Int.MAX_VALUE
                val filteredCues = filterCues(0, endPosition - 1)
                if (filteredCues.isNotEmpty()) {
                    view?.showCuePicker(IDX_FROM, filteredCues)
                }
            }
            IDX_TO -> {
                val startPosition = selectedStart?.position ?: 0
                val filteredCues = filterCues(startPosition + 1, Int.MAX_VALUE)
                if (filteredCues.isNotEmpty()) {
                    view?.showCuePicker(IDX_TO, filteredCues)
                }
            }
        }
    }

    private fun filterCues(from: Int, to: Int)
            : List<Pair<Long, String>> {
        return cues
                .filter {
                    it.position in from..to
                }
                .map {
                    val abstract = it.content.getAbstract(ABSTRACT_LENGTH)
                    val oneLine = "${it.character?.name.orEmpty()}\n$abstract\n"
                    it.cueId to oneLine
                }
    }


    override fun onScenePicked(sceneId: Long) {
        val previousSceneId = selectedScene?.sceneId
        selectedScene = scenes.firstOrNull { it.sceneId == sceneId }

        if (previousSceneId != sceneId) {
            selectedStart = null
            selectedEnd = null
            cues = emptyList()

            cuesDisposable?.dispose()
            cuesDisposable = cuesDataSourceProvider(sceneId).getData()
                    .schedule(schedulerProvider)
                    .subscribe(
                            {
                                cues = it.filter { cue -> cue.type == CueDbModel.TYPE_DIALOG }
                                updateCueSelection()
                            },
                            { view?.showError(it) }
                    )
            updateSceneSelection()
        }

    }

    override fun onCuePicked(requestId: Int, cueId: Long) {
        val cue = cues.firstOrNull { it.cueId == cueId }

        when (requestId) {
            IDX_FROM -> selectedStart = cue
            IDX_TO -> selectedEnd = cue
        }

        updateForm()
    }

    override fun onValidate() {
        val scene = selectedScene ?: return
        val start = selectedStart ?: return
        val end = selectedEnd ?: return

        val range = Range(
                rangeId = 0,
                rehearsalId = rehearsalId,
                fromCue = start,
                toCue = end,
                scene = scene
        )
        view?.navigateBackWithResult(range)
    }

    // endregion

    // region Internal


    private fun updateSceneSelection() {

        if (selectedScene != null) {
            val scene = scenes.firstOrNull { it.sceneId == selectedScene?.sceneId }
            if (scene == null) {
                selectedStart = null
                selectedEnd = null
            }

            selectedScene = scene
        } else {
            selectedStart = null
            selectedEnd = null
            cues = emptyList()
        }

        updateForm()
    }


    private fun updateCueSelection() {
        val start = if (selectedStart == null) {
            cues.firstOrNull()
        } else {
            cues.firstOrNull { it.cueId == selectedStart?.cueId }
        }

        val end = if (selectedEnd == null) {
            cues.lastOrNull()
        } else {
            cues.firstOrNull { it.cueId == selectedEnd?.cueId }
        }

        selectedStart = start
        selectedEnd = end

        updateForm()
    }

    private fun updateForm() {
        view?.showData(prepareForm())

        val isValid = selectedScene != null && selectedStart != null && selectedEnd != null
        view?.showValidate(isValid)
    }

    private fun prepareForm(): List<Item.ViewModel> {
        val list = mutableListOf<Item.ViewModel>()

        val defaultCue = if (cues.isEmpty()) "✗" else "?"

        val sceneDesc = selectedScene?.description()

        list.add(ItemInteractive.ViewModel(
                id = StableId.getStableId(0, IDX_SCENE, Item.Type.INTERACTIVE.ordinal),
                labelRes = R.string.rangePicker_action_scene,
                value = sceneDesc ?: "?"
        ))

        list.add(ItemInteractive.ViewModel(
                id = StableId.getStableId(0, IDX_FROM, Item.Type.INTERACTIVE.ordinal),
                labelRes = R.string.rangePicker_action_from,
                value = selectedStart?.content?.getAbstract(ABSTRACT_LENGTH) ?: defaultCue
        ))

        list.add(ItemInteractive.ViewModel(
                id = StableId.getStableId(0, IDX_TO, Item.Type.INTERACTIVE.ordinal),
                labelRes = R.string.rangePicker_action_to,
                value = selectedEnd?.content?.getAbstract(ABSTRACT_LENGTH) ?: defaultCue
        ))

        return list
    }


    fun Scene.description(): String {
        return "($cuesCount) ­— $title"
    }

    // endregion

    companion object {

        const val IDX_SCENE = 1
        const val IDX_FROM = 2
        const val IDX_TO = 3


        private const val ABSTRACT_LENGTH = 24
    }
}
