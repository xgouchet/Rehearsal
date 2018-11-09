package fr.xgouchet.rehearsal.scene

import androidx.lifecycle.LifecycleOwner
import fr.xgouchet.archx.ArchXView
import fr.xgouchet.archx.data.ArchXDataPresenter
import fr.xgouchet.rehearsal.core.room.join.CueWithCharacter
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.voice.app.VoiceController
import fr.xgouchet.rehearsal.voice.app.VoiceServiceListener

class ScenePresenter(
        private val sceneId: Int,
        private val voiceController: VoiceController,
        owner: LifecycleOwner,
        dataSource: SceneContract.DataSource,
        dataSink: SceneContract.DataSink,
        transformer: SceneContract.Transformer
) : ArchXDataPresenter<List<CueWithCharacter>, SceneContract.View, List<Item.ViewModel>>(owner, dataSource, dataSink, transformer),
        SceneContract.Presenter,
        VoiceServiceListener {

    private var linesVisible: Boolean = false
    private var rawData: List<CueWithCharacter> = emptyList()
    private var bookmarkedCues: List<CueWithCharacter> = emptyList()
    private var viewModelData: List<Item.ViewModel> = emptyList()

    private var activeCueId: Int = -1
    private var isReading: Boolean = false

    // region ArchXDataPresenter

    override fun onViewAttached(view: ArchXView<List<Item.ViewModel>>, isRestored: Boolean) {
        super.onViewAttached(view, isRestored)
        owner.lifecycle.addObserver(voiceController)
        voiceController.listener = this

        this.view?.showLinesVisible(linesVisible)
        this.view?.showReading(isReading)
    }

    override fun onChanged(t: List<CueWithCharacter>) {
        rawData = t
        bookmarkedCues = rawData.filter { it.isBookmarked }
        updateView()
    }

    // endregion

    // region SceneContract.Presenter

    override fun onItemSelected(item: Item.ViewModel) {
        val selectedCue = item.getItemData() as? CueWithCharacter
        if (selectedCue != null) {
            val cueId = selectedCue.cueId
            if (isReading) {
                voiceController.playFromCue(sceneId, cueId)
            } else {
                setActiveCue(cueId, false)
            }
        }
    }

    override fun onItemPressed(item: Item.ViewModel) {
        val selectedCue = item.getItemData() as? CueWithCharacter
        if (selectedCue != null) {
            view?.showContextMenu(CueMenuContext(
                    cueId = selectedCue.cueId,
                    isBookmarked = selectedCue.isBookmarked
            ))
        }
    }

    override fun onAddBookmarkPicked(cueId: Int) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }
        if (selectedCue != null && !selectedCue.isBookmarked) {
            val updatedCue = selectedCue.copy(isBookmarked = true)
            dataSink.updateData(listOf(updatedCue))
        }
    }

    override fun onRemoveBookmarkPicked(cueId: Int) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }
        if (selectedCue != null && selectedCue.isBookmarked) {
            val updatedCue = selectedCue.copy(isBookmarked = false)
            dataSink.updateData(listOf(updatedCue))
        }
    }

    override fun onBookmarkPicked(cueId: Int) {
        if (isReading) {
            voiceController.playFromCue(sceneId, cueId)
        } else {
            setActiveCue(cueId, true)
        }
    }

    override fun onLinesVisibilityChanged(linesVisible: Boolean) {
        this.linesVisible = linesVisible
        (transformer as? SceneContract.Transformer)?.setUserLinesVisible(linesVisible)
        updateView()
        view?.showLinesVisible(linesVisible)
    }

    override fun onPlayPauseSelected() {
        if (isReading) {
            voiceController.stop()
        } else {
            val firstCueId = rawData.firstOrNull()?.cueId ?: -1
            val startFromCue = if (activeCueId >= 0) activeCueId else firstCueId

            voiceController.playFromCue(sceneId, startFromCue)
        }
    }

    override fun onGoToBookmarkSelected() {
        val map = bookmarkedCues.map { it.cueId to "(${it.position}) ${it.character?.name.orEmpty()}\n${it.content}" }
        view?.showBookmarksDialog(map)
    }

    // endregion

    // region VoiceServiceListener

    override fun readingCue(cueId: Int) {
        isReading = true
        setActiveCue(cueId, true)
        view?.showReading(true)

        val index = viewModelData.indexOfFirst {
            val cueWithCharacter = it.getItemData() as? CueWithCharacter
            cueWithCharacter?.cueId == cueId && it.getItemType() != Item.Type.CHARACTER
        }
        if (index >= 0) {
            view?.scrollToRow(index)
        }
    }

    override fun stopped() {
        isReading = false
        view?.showReading(false)
    }

    // endregion

    // region Internal

    private fun setActiveCue(cueId: Int, scrollToCue: Boolean) {
        (transformer as? SceneContract.Transformer)?.setSelectedCue(cueId)
        activeCueId = cueId
        updateView()

        if (scrollToCue) {
            val index = viewModelData.indexOfFirst {
                val cueWithCharacter = it.getItemData() as? CueWithCharacter
                cueWithCharacter?.cueId == cueId && it.getItemType() != Item.Type.CHARACTER
            }
            if (index >= 0) {
                view?.scrollToRow(index)
            }
        }
    }

    private fun updateView() {
        viewModelData = transformer.transform(rawData)
        view?.showData(viewModelData)
    }

    // endregion
}
