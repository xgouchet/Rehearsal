package fr.xgouchet.rehearsal.screen.scene

import fr.xgouchet.archx.ArchXPresenter
import fr.xgouchet.archx.ArchXView
import fr.xgouchet.archx.transformer.ArchXViewModelTransformer
import fr.xgouchet.rehearsal.core.model.Cue
import fr.xgouchet.rehearsal.ui.Item

interface SceneContract {

    interface Presenter : ArchXPresenter<List<Item.ViewModel>> {
        fun onItemSelected(item: Item.ViewModel)
        fun onItemPressed(item: Item.ViewModel)

        fun onLinesVisibilityChanged(linesVisible: Boolean)
        fun onPlayPauseSelected()

        fun onGoToBookmarkSelected()
        fun onBookmarkPicked(cueId: Long)
        fun onAddBookmarkPicked(cueId: Long)
        fun onRemoveBookmarkPicked(cueId: Long)

        fun onAddNotePicked(cueId: Long)
        fun onShowNotePicked(cueId: Long)
        fun onEditNotePicked(cueId: Long)
        fun onRemoveNotesPicked(cueId: Long)
        fun onNoteEdited(cueId: Long, note: String)

        fun onEditCuePicked(cueId: Long)
        fun onCueEdited(cueId: Long, content: String, c: CharacterInfo)
        fun onDeleteCue(cueId: Long)
        fun onDeleteCueConfirmed(cueId: Long)
        fun onAddDialog(cueId: Long)
        fun onDialogWritten(cueId: Long, content: String, c: CharacterInfo)
        fun onAddAction(cueId: Long)
        fun onActionWritten(cueId: Long, content: String, c: CharacterInfo)
        fun onAddLyrics(cueId: Long)
        fun onLyricsWritten(cueId: Long, content: String, c: CharacterInfo)
        fun onCopyCue(cueId: Long)
    }

    interface View : ArchXView<List<Item.ViewModel>> {
        fun showLinesVisible(linesVisible: Boolean)

        fun showReading(reading: Boolean)
        fun scrollToRow(index: Int)

        fun showContextMenu(context: CueInfo)

        fun showBookmarksDialog(bookmarks: List<Pair<Long, String>>)
        fun showHasBookmarks(hasBookmarks: Boolean)

        fun showNotePrompt(cueId: Long, title: String, note: String)
        fun showNote(note: String)

        fun showEditCuePrompt(cueId: Long, content: String, characters: List<CharacterInfo>, selected: CharacterInfo?)
        fun showDeleteConfirm(cueId: Long, title: String)
        fun showAddActionPrompt(afterCueId: Long, characters: List<CharacterInfo>, selected: CharacterInfo?)
        fun showAddDialogPrompt(afterCueId: Long, characters: List<CharacterInfo>, selected: CharacterInfo?)
        fun showAddLyricsPrompt(afterCueId: Long, characters: List<CharacterInfo>, selected: CharacterInfo?)

        fun showError(throwable: Throwable)
        fun copyToClipboard(label: String, content: String)
    }


    interface Transformer
        : ArchXViewModelTransformer<List<Cue>, List<Item.ViewModel>> {
        fun setUserLinesVisible(visible: Boolean)
        fun setSelectedCue(cueId: Long)
    }
}
