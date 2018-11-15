package fr.xgouchet.rehearsal.scene

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import fr.xgouchet.archx.ArchXView
import fr.xgouchet.archx.data.ArchXDataPresenter
import fr.xgouchet.rehearsal.core.room.join.CueWithCharacter
import fr.xgouchet.rehearsal.core.room.model.CharacterModel
import fr.xgouchet.rehearsal.core.room.model.CueModel
import fr.xgouchet.rehearsal.ext.getAbstract
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.voice.app.VoiceController
import fr.xgouchet.rehearsal.voice.app.VoiceServiceListener

class ScenePresenter(
        private val sceneId: Int,
        private val voiceController: VoiceController,
        owner: LifecycleOwner,
        dataSource: SceneContract.DataSource,
        private val dataSink: SceneContract.DataSink,
        private val characterDataSource: SceneContract.CharacterDataSource,
        transformer: SceneContract.Transformer
) : ArchXDataPresenter<List<CueWithCharacter>, SceneContract.View, List<Item.ViewModel>>(owner, dataSource, transformer),
        SceneContract.Presenter,
        VoiceServiceListener {

    private var linesVisible: Boolean = false
    private var rawData: List<CueWithCharacter> = emptyList()
    private var bookmarkedCues: List<CueWithCharacter> = emptyList()
    private var viewModelData: List<Item.ViewModel> = emptyList()
    private var characters: List<CharacterModel> = emptyList()

    private val characterObserver = Observer<List<CharacterModel>> { list -> characters = list }

    private var activeCueId: Int = -1
    private var isReading: Boolean = false

    // region ArchXDataPresenter

    override fun onViewAttached(view: ArchXView<List<Item.ViewModel>>, isRestored: Boolean) {
        super.onViewAttached(view, isRestored)
        owner.lifecycle.addObserver(voiceController)
        voiceController.listener = this

        this.view?.showLinesVisible(linesVisible)
        this.view?.showReading(isReading)

        characterDataSource.getData().observe(owner, characterObserver)
    }

    override fun onViewDetached() {
        super.onViewDetached()
        characterDataSource.getData().removeObserver(characterObserver)
    }


    override fun onChanged(t: List<CueWithCharacter>) {
        rawData = t
        bookmarkedCues = rawData.filter { it.isBookmarked }
        view?.showHasBookmarks(bookmarkedCues.isNotEmpty())
        updateView()
    }

    // endregion

    // region SceneContract.Presenter / Direct Interaction

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

            voiceController.playFromCue(sceneId, startFromCue)
        }
    }

    // endregion

    // region SceneContract.Presenter / Scene Edition

    override fun onEditCuePicked(cueId: Int) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }
        if (selectedCue != null) {
            val characterInfo = getCharacterInfoList(selectedCue.type != CueModel.TYPE_DIALOG)
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

    override fun onCueEdited(cueId: Int, content: String, c: CharacterInfo) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }
        val selectedCharacter = characters.firstOrNull { it.characterId == c.characterId }
        if (selectedCue != null) {
            val updatedCue = selectedCue.copy(content = content, character = selectedCharacter)
            dataSink.updateData(listOf(updatedCue)) {
                if (it != null) {
                    view?.showError(it)
                }
            }
        }
    }

    override fun onDeleteCue(cueId: Int) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }
        if (selectedCue != null) {
            val abstract = selectedCue.content.getAbstract(CONTEXT_MENU_ABSTRACT_LENGTH)
            view?.showDeleteConfirm(cueId, abstract)
        }
    }

    override fun onDeleteCueConfirmed(cueId: Int) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }
        if (selectedCue != null) {
            dataSink.deleteData(listOf(selectedCue)) {
                if (it != null) {
                    view?.showError(it)
                }
            }
        }
    }

    override fun onAddDialog(cueId: Int) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }
        if (selectedCue != null) {
            val characterInfo = getCharacterInfoList(false)
            val selectedCharacter = characterInfo.firstOrNull { it.characterId == selectedCue.character?.characterId }
            view?.showAddDialogPrompt(cueId, characterInfo, selectedCharacter)
        }
    }

    override fun onDialogWritten(cueId: Int, content: String, c: CharacterInfo) {
        val selectedCharacter = characters.firstOrNull { it.characterId == c.characterId } ?: return
        addCueAfter(
                cueId = cueId,
                content = content,
                type = CueModel.TYPE_DIALOG,
                character = selectedCharacter
        )
    }


    override fun onAddAction(cueId: Int) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }
        if (selectedCue != null) {
            val characterInfo = getCharacterInfoList(true)
            val selectedCharacter = characterInfo.firstOrNull { it.characterId == selectedCue.character?.characterId }
            view?.showAddActionPrompt(cueId, characterInfo, selectedCharacter)
        }
    }

    override fun onActionWritten(cueId: Int, content: String, c: CharacterInfo) {
        val selectedCharacter = characters.firstOrNull { it.characterId == c.characterId }
        addCueAfter(
                cueId = cueId,
                content = content,
                type = CueModel.TYPE_ACTION,
                character = selectedCharacter
        )
    }

    override fun onAddLyrics(cueId: Int) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }
        if (selectedCue != null) {
            val characterInfo = getCharacterInfoList(true)
            val selectedCharacter = characterInfo.firstOrNull { it.characterId == selectedCue.character?.characterId }
            view?.showAddLyricsPrompt(cueId, characterInfo, selectedCharacter)
        }
    }

    override fun onLyricsWritten(cueId: Int, content: String, c: CharacterInfo) {
        val selectedCharacter = characters.firstOrNull { it.characterId == c.characterId }
        addCueAfter(
                cueId = cueId,
                content = content,
                type = CueModel.TYPE_LYRICS,
                character = selectedCharacter
        )
    }
    // endregion

    // region SceneContract.Presenter / Bookmarks


    override fun onAddBookmarkPicked(cueId: Int) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }
        if (selectedCue != null && !selectedCue.isBookmarked) {
            val updatedCue = selectedCue.copy(isBookmarked = true)
            dataSink.updateData(listOf(updatedCue)) {
                if (it != null) {
                    view?.showError(it)
                }
            }
        }
    }

    override fun onRemoveBookmarkPicked(cueId: Int) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }
        if (selectedCue != null && selectedCue.isBookmarked) {
            val updatedCue = selectedCue.copy(isBookmarked = false)
            dataSink.updateData(listOf(updatedCue)) {
                if (it != null) {
                    view?.showError(it)
                }
            }
        }
    }

    override fun onBookmarkPicked(cueId: Int) {
        if (isReading) {
            voiceController.playFromCue(sceneId, cueId)
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

    override fun onAddNotePicked(cueId: Int) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }
        if (selectedCue != null) {
            view?.showNotePrompt(cueId, selectedCue.content.getAbstract(CONTEXT_MENU_ABSTRACT_LENGTH), "")
        }
    }

    override fun onShowNotePicked(cueId: Int) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }
        val note = selectedCue?.note.orEmpty()
        if (note.isNotBlank()) {
            view?.showNote(note)
        }
    }

    override fun onEditNotePicked(cueId: Int) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }
        if (selectedCue != null) {
            view?.showNotePrompt(cueId, selectedCue.content.getAbstract(CONTEXT_MENU_ABSTRACT_LENGTH), selectedCue.note.orEmpty())
        }
    }

    override fun onRemoveNotesPicked(cueId: Int) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }
        if (selectedCue != null) {
            val updatedCue = selectedCue.copy(note = null)
            dataSink.updateData(listOf(updatedCue)) {
                if (it != null) {
                    view?.showError(it)
                }
            }
        }
    }

    override fun onNoteEdited(cueId: Int, note: String) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }
        if (selectedCue != null) {
            val updatedCue = selectedCue.copy(note = note)
            dataSink.updateData(listOf(updatedCue)) {
                if (it != null) {
                    view?.showError(it)
                }
            }
        }
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


    private fun addCueAfter(cueId: Int,
                            content: String,
                            type: Int,
                            character: CharacterModel?) {
        val selectedCue = rawData.firstOrNull { it.cueId == cueId }
        if (selectedCue != null) {

            val movedCues = rawData.filter { it.position > selectedCue.position }
                    .map { it.copy(position = it.position + 1) }

            val newCue = CueWithCharacter(
                    cueId = 0,
                    position = selectedCue.position + 1,
                    character = character,
                    isBookmarked = false,
                    note = null,
                    sceneId = sceneId,
                    type = type,
                    content = content,
                    characterExtension = null
            )

            if (movedCues.isNotEmpty()) {
                dataSink.updateData(movedCues) {
                    if (it != null) {
                        view?.showError(it)
                    }
                }
            }
            dataSink.createData(listOf(newCue)) {
                if (it != null) {
                    view?.showError(it)
                }
            }
        }
    }

    private fun getBookmarkDescription(cue: CueWithCharacter): String {
        val abstract = cue.content.getAbstract(BOOKMARK_ABSTRACT_LENGHT)

        return "${cue.character?.name.orEmpty()}\n$abstract\n"
    }


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

    companion object {
        private const val CONTEXT_MENU_ABSTRACT_LENGTH = 24
        private const val BOOKMARK_ABSTRACT_LENGHT = 32
    }
}
