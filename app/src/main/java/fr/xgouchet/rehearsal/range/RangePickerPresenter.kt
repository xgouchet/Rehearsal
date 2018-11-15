package fr.xgouchet.rehearsal.range

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import fr.xgouchet.archx.ArchXView
import fr.xgouchet.rehearsal.R
import fr.xgouchet.rehearsal.core.room.join.CueWithCharacter
import fr.xgouchet.rehearsal.core.room.join.SceneWithCount
import fr.xgouchet.rehearsal.core.room.model.CueModel
import fr.xgouchet.rehearsal.core.room.model.RangeModel
import fr.xgouchet.rehearsal.ext.getAbstract
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemInteractive
import fr.xgouchet.rehearsal.ui.StableId

class RangePickerPresenter(
        private val scriptId: Int,
        private val owner: LifecycleOwner,
        private val sceneDataSource: RangePickerContract.SceneDataSource,
        private val cueDataSourceProvider: (Int) -> RangePickerContract.CueDataSource
) : RangePickerContract.Presenter {


    protected var view: RangePickerContract.View? = null

    private var scenes: List<SceneWithCount> = emptyList()
    private var selectedScene: SceneWithCount? = null
    private val scenesObserver = Observer<List<SceneWithCount>> {
        scenes = it
        updateSceneSelection()
    }

    private var cues: List<CueWithCharacter> = emptyList()
    private var selectedStart: CueWithCharacter? = null
    private var selectedEnd: CueWithCharacter? = null
    private var cueData: LiveData<List<CueWithCharacter>>? = null
    private val cuesObserver = Observer<List<CueWithCharacter>> {
        cues = it.filter { cue -> cue.type == CueModel.TYPE_DIALOG }
        updateCueSelection()
    }

    // region ArchXContract.Presenter

    override fun onViewAttached(view: ArchXView<List<Item.ViewModel>>, isRestored: Boolean) {
        this.view = view as? RangePickerContract.View
        this.view?.bindPresenter(this)

        sceneDataSource.getData().observe(owner, scenesObserver)
        updateForm()
    }

    override fun onViewDetached() {
        view = null

        sceneDataSource.getData().removeObserver(scenesObserver)
        cueData?.removeObserver(cuesObserver)
    }

    // endregion

    // region RangePickerContract.Presenter

    override fun onItemSelected(item: Item.ViewModel) {
        val subIndex = StableId.getSubIndex(item.getItemStableId())

        when (subIndex) {
            IDX_SCENE -> {
                view?.showScenePicker(scenes.map { it.sceneId to "(${it.cues}) ­— ${it.description}" })
            }
            IDX_FROM, IDX_TO -> {
                val filteredCues = cues.map {
                    val abstract = it.content.getAbstract(ABSTRACT_LENGTH)
                    val oneLine = "${it.character?.name.orEmpty()}\n$abstract\n"
                    it.cueId to oneLine
                }
                view?.showCuePicker(subIndex, filteredCues)
            }
        }
    }

    override fun onScenePicked(sceneId: Int) {
        selectedScene = scenes.firstOrNull { it.sceneId == sceneId }
        cues = emptyList()

        cueData?.removeObserver(cuesObserver)
        cueData = cueDataSourceProvider(sceneId).getData().apply { observe(owner, cuesObserver) }

        updateSceneSelection()
    }

    override fun onCuePicked(requestId: Int, cueId: Int) {
        val cue = cues.firstOrNull { it.cueId == cueId }

        when (requestId) {
            IDX_FROM -> selectedStart = cue
            IDX_TO -> selectedEnd = cue
        }

        updateForm()
    }

    override fun onValidate() {
        val rangeModel = RangeModel(
                rangeId = 0,
                scheduleId = 0,
                startCue = selectedStart?.asCueModel(),
                endCue = selectedEnd?.asCueModel(),
                scene = selectedScene?.asSceneModel()
        )
        view?.navigateBackWithResult(rangeModel)
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
        // TODO swap start and end if they're out of order

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

        val sceneDesc = selectedScene?.let {
            "(${it.cues}) ­— ${it.description}"
        }
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

    // endregion

    companion object {

        const val IDX_SCENE = 1
        const val IDX_FROM = 2
        const val IDX_TO = 3


        private const val ABSTRACT_LENGTH = 24
    }
}
