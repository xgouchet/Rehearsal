package fr.xgouchet.rehearsal.screen.scene

import fr.xgouchet.archx.ArchXView
import fr.xgouchet.archx.data.ArchXDataPresenter
import fr.xgouchet.archx.data.ArchXDataSink
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.archx.rx.SchedulerProvider
import fr.xgouchet.archx.rx.schedule
import fr.xgouchet.rehearsal.core.model.Character
import fr.xgouchet.rehearsal.core.model.Cue
import fr.xgouchet.rehearsal.core.model.Scene
import fr.xgouchet.rehearsal.core.room.model.CueDbModel
import fr.xgouchet.rehearsal.ext.getAbstract
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.voice.app.VoiceController
import fr.xgouchet.rehearsal.voice.app.VoiceServiceListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

class ScenePresenter(
        private val scene: Scene,
        private var range: Pair<Int, Int>?,
        private val voiceController: VoiceController,
        dataSource: ArchXDataSource<List<Cue>>,
        private val characterDataSource: ArchXDataSource<List<Character>>,
        private val dataSink: ArchXDataSink<List<Cue>>,
        transformer: SceneContract.Transformer,
        schedulerProvider: SchedulerProvider
) : ArchXDataPresenter<List<Cue>, SceneContract.View, List<Item.ViewModel>>(dataSource, transformer, schedulerProvider),
        SceneContract.Presenter,
        VoiceServiceListener {

    private var linesVisible: Boolean = false
    private var rawData: List<Cue> = emptyList()
    private var bookmarkedCues: List<Cue> = emptyList()
    private var viewModelData: List<Item.ViewModel> = emptyList()
    private var characters: List<Character> = emptyList()


    private val editingCompositeDisposable = CompositeDisposable()
    private var listeningCharactersDisposable: Disposable? = null

    private var activeCueId: Long = -1
    private var isReading: Boolean = false

    // region ArchXDataPresenter

    override fun onViewAttached(view: ArchXView<List<Item.ViewModel>>, isRestored: Boolean) {
        super.onViewAttached(view, isRestored)
        voiceController.listener = this

        this.view?.showLinesVisible(linesVisible)
        this.view?.showReading(isReading)

        listeningCharactersDisposable = characterDataSource.listenData()
                .schedule(schedulerProvider)
                .subscribe(
                        { characters = it },
                        { Timber.e(it, "#error listening to characters @scene:$scene") }
                )
    }

    override fun onViewDetached() {
        super.onViewDetached()

        listeningCharactersDisposable?.dispose()
        listeningCharactersDisposable = null

        editingCompositeDisposable.clear()
    }

    override fun onDataChanged(t: List<Cue>) {
        rawData = t
        bookmarkedCues = rawData.filter { it.isBookmarked }
        view?.showHasBookmarks(bookmarkedCues.isNotEmpty())
        updateView()
    }

    // endregion

    // region SceneContract.Presenter / Direct Interaction

    override fun onItemSelected(item: Item.ViewModel) {
        val selectedCue = item.getItemData() as? Cue
        if (selectedCue != null) {
            val cueId = selectedCue.cueId
            if (isReading) {
                voiceController.playFromCue(scene.sceneId, cueId)
            } else {
                setActiveCue(cueId, false)
            }
        }
    }

    override fun onItemPressed(item: Item.ViewModel) {
        val selectedCue = item.getItemData() as? Cue
        if (selectedCue != null) {
            view?.showContextMenu(CueInfo(
                    cueId = selectedCue.cueId,
                    abstract = selectedCue.content.getAbstract(CONTEXT_MENU_ABSTRACT_LENGTH),
                    isBookmarked = selectedCue.isBookmarked,
                    hasNote = !selectedCue.note.isNullOrBlank()
            ))
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

            voiceController.playFromCue(scene.sceneId, startFromCue)
        }
    }

    // endregion

    // region SceneContract.Presenter / Scene Edition

    override fun onCopyCue(cueId: Long) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }

        if (selectedCue != null) {
            val character = selectedCue?.character
            val labelType = when (selectedCue.type) {
                CueDbModel.TYPE_DIALOG -> "line"
                CueDbModel.TYPE_ACTION -> "action"
                CueDbModel.TYPE_LYRICS -> "lyrics"
                else -> "cue"
            }

            val content = selectedCue.content
            val label = if (character != null) {
                "${character.name}'s $labelType: ${content.getAbstract(SHORT_ABSTRACT_LENGTH)}"
            } else {
                "$labelType: ${content.getAbstract(CONTEXT_MENU_ABSTRACT_LENGTH)}"
            }
            view?.copyToClipboard(label, content)
        }

    }

    override fun onEditCuePicked(cueId: Long) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }
        if (selectedCue != null) {
            val characterInfo = getCharacterInfoList(selectedCue.type != CueDbModel.TYPE_DIALOG)
            val selectedCharacter = characterInfo.firstOrNull { it.characterId == selectedCue.character?.characterId }
            view?.showEditCuePrompt(cueId, selectedCue.content, characterInfo, selectedCharacter)
        }
    }

    private fun getCharacterInfoList(withNull: Boolean): List<CharacterInfo> {

        val knownCharacters = characters.map { CharacterInfo(it.characterId, it.name) }


        return if (withNull) {
            arrayOf(CharacterInfo(0, " — "))
                    .union(knownCharacters)
                    .toList()
        } else {
            knownCharacters
        }
    }

    override fun onCueEdited(cueId: Long, content: String, c: CharacterInfo) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }
        val selectedCharacter = characters.firstOrNull { it.characterId == c.characterId }
        if (selectedCue != null) {
            val updatedCue = selectedCue.copy(content = content, character = selectedCharacter)
            updateCue(updatedCue)
        }
    }

    override fun onDeleteCue(cueId: Long) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }
        if (selectedCue != null) {
            val abstract = selectedCue.content.getAbstract(CONTEXT_MENU_ABSTRACT_LENGTH)
            view?.showDeleteConfirm(cueId, abstract)
        }
    }

    override fun onDeleteCueConfirmed(cueId: Long) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }
        if (selectedCue != null) {
            val disposable = dataSink.deleteData(listOf(selectedCue))
                    .schedule(schedulerProvider)
                    .subscribe(
                            { Timber.i("#deleted @cue:$it") },
                            { view?.showError(it) }
                    )
            editingCompositeDisposable.add(disposable)
        }
    }

    override fun onAddDialog(cueId: Long) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }
        if (selectedCue != null) {
            val characterInfo = getCharacterInfoList(false)
            val selectedCharacter = characterInfo.firstOrNull { it.characterId == selectedCue.character?.characterId }
            view?.showAddDialogPrompt(cueId, characterInfo, selectedCharacter)
        }
    }

    override fun onDialogWritten(cueId: Long, content: String, c: CharacterInfo) {
        val selectedCharacter = characters.firstOrNull { it.characterId == c.characterId } ?: return
        addCueAfter(
                cueId = cueId,
                content = content,
                type = CueDbModel.TYPE_DIALOG,
                character = selectedCharacter
        )
    }


    override fun onAddAction(cueId: Long) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }
        if (selectedCue != null) {
            val characterInfo = getCharacterInfoList(true)
            val selectedCharacter = characterInfo.firstOrNull { it.characterId == selectedCue.character?.characterId }
            view?.showAddActionPrompt(cueId, characterInfo, selectedCharacter)
        }
    }

    override fun onActionWritten(cueId: Long, content: String, c: CharacterInfo) {
        val selectedCharacter = characters.firstOrNull { it.characterId == c.characterId }
        addCueAfter(
                cueId = cueId,
                content = content,
                type = CueDbModel.TYPE_ACTION,
                character = selectedCharacter
        )
    }

    override fun onAddLyrics(cueId: Long) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }
        if (selectedCue != null) {
            val characterInfo = getCharacterInfoList(true)
            val selectedCharacter = characterInfo.firstOrNull { it.characterId == selectedCue.character?.characterId }
            view?.showAddLyricsPrompt(cueId, characterInfo, selectedCharacter)
        }
    }

    override fun onLyricsWritten(cueId: Long, content: String, c: CharacterInfo) {
        val selectedCharacter = characters.firstOrNull { it.characterId == c.characterId }
        addCueAfter(
                cueId = cueId,
                content = content,
                type = CueDbModel.TYPE_LYRICS,
                character = selectedCharacter
        )
    }

    // endregion

    // region SceneContract.Presenter / Bookmarks


    override fun onAddBookmarkPicked(cueId: Long) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }
        if (selectedCue != null && !selectedCue.isBookmarked) {
            val updatedCue = selectedCue.copy(isBookmarked = true)
            updateCue(updatedCue)
        }
    }

    override fun onRemoveBookmarkPicked(cueId: Long) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }
        if (selectedCue != null && selectedCue.isBookmarked) {
            val updatedCue = selectedCue.copy(isBookmarked = false)
            updateCue(updatedCue)
        }
    }

    override fun onBookmarkPicked(cueId: Long) {
        if (isReading) {
            voiceController.playFromCue(scene.sceneId, cueId)
        } else {
            setActiveCue(cueId, true)
        }
    }

    override fun onGoToBookmarkSelected() {
        val map = bookmarkedCues.map { it.cueId to getBookmarkDescription(it) }
        view?.showBookmarksDialog(map)
    }

    // endregion

    // region SceneContract.Presenter / Notes

    override fun onAddNotePicked(cueId: Long) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }
        if (selectedCue != null) {
            view?.showNotePrompt(cueId, selectedCue.content.getAbstract(CONTEXT_MENU_ABSTRACT_LENGTH), "")
        }
    }

    override fun onShowNotePicked(cueId: Long) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }
        val note = selectedCue?.note.orEmpty()
        if (note.isNotBlank()) {
            view?.showNote(note)
        }
    }

    override fun onEditNotePicked(cueId: Long) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }
        if (selectedCue != null) {
            view?.showNotePrompt(cueId, selectedCue.content.getAbstract(CONTEXT_MENU_ABSTRACT_LENGTH), selectedCue.note.orEmpty())
        }
    }

    override fun onRemoveNotesPicked(cueId: Long) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }
        if (selectedCue != null) {
            val updatedCue = selectedCue.copy(note = null)
            updateCue(updatedCue)
        }
    }

    override fun onNoteEdited(cueId: Long, note: String) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }
        if (selectedCue != null) {
            val updatedCue = selectedCue.copy(note = note)
            updateCue(updatedCue)
        }
    }


    // endregion

    // region VoiceServiceListener

    override fun readingCue(cueId: Long) {
        isReading = true
        setActiveCue(cueId, true)
        view?.showReading(true)

        val index = viewModelData.indexOfFirst {
            val row = it.getItemData() as? Cue
            row?.cueId == cueId && it.getItemType() != Item.Type.CHARACTER
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


    private fun addCueAfter(cueId: Long,
                            content: String,
                            type: Int,
                            character: Character?) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }
        if (selectedCue != null) {


            val currentRange = range
            range = currentRange?.copy(second = currentRange.second + 1)

            val movedCues = rawData.filter { it.position > selectedCue.position }
                    .map { it.copy(position = it.position + 1) }

            val newCue = Cue(
                    cueId = 0,
                    position = selectedCue.position + 1,
                    character = character,
                    isBookmarked = false,
                    note = null,
                    sceneId = scene.sceneId,
                    type = type,
                    content = content,
                    characterExtension = null
            )

            if (movedCues.isNotEmpty()) {
                val disposable = dataSink.updateData(movedCues)
                        .schedule(schedulerProvider)
                        .subscribe(
                                { createCue(newCue) },
                                { view?.showError(it) }
                        )
                editingCompositeDisposable.add(disposable)
            } else {
                createCue(newCue)
            }
        }
    }

    private fun getBookmarkDescription(cue: Cue): String {
        val abstract = cue.content.getAbstract(BOOKMARK_ABSTRACT_LENGHT)

        return "${cue.character?.name.orEmpty()}\n$abstract\n"
    }


    private fun setActiveCue(cueId: Long, scrollToCue: Boolean) {
        (transformer as? SceneContract.Transformer)?.setSelectedCue(cueId)
        activeCueId = cueId
        updateView()

        if (scrollToCue) {
            val index = viewModelData.indexOfFirst {
                val Cue = it.getItemData() as? Cue
                Cue?.cueId == cueId && it.getItemType() != Item.Type.CHARACTER
            }
            if (index >= 0) {
                view?.scrollToRow(index)
            }
        }
    }

    private fun updateView() {
        val list = rawData
        val currentRange = range
        val rangeList = if (currentRange == null) list else list.filter { it.position in currentRange.first..currentRange.second }

        viewModelData = transformer.transform(rangeList)
        view?.showData(viewModelData)
    }

    private fun updateCue(updatedCue: Cue) {
        val disposable = dataSink.updateData(listOf(updatedCue))
                .schedule(schedulerProvider)
                .subscribe(
                        { Timber.i("#updated @cue:$it") },
                        { view?.showError(it) }
                )
        editingCompositeDisposable.add(disposable)
    }

    private fun createCue(newCue: Cue) {
        val disposable = dataSink.createData(listOf(newCue))
                .schedule(schedulerProvider)
                .subscribe(
                        { Timber.i("#created @cue:$it") },
                        { view?.showError(it) }
                )
        editingCompositeDisposable.add(disposable)
    }

    // endregion

    companion object {
        private const val SHORT_ABSTRACT_LENGTH = 16
        private const val CONTEXT_MENU_ABSTRACT_LENGTH = 24
        private const val BOOKMARK_ABSTRACT_LENGHT = 32
    }
}
