package fr.xgouchet.rehearsal.scene

import fr.xgouchet.archx.ArchXPresenter
import fr.xgouchet.archx.ArchXView
import fr.xgouchet.archx.ArchXViewModelTransformer
import fr.xgouchet.archx.data.ArchXDataSink
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.rehearsal.core.room.join.CueWithCharacter
import fr.xgouchet.rehearsal.core.room.model.CharacterModel
import fr.xgouchet.rehearsal.ui.Item

interface SceneContract {

    interface Presenter : ArchXPresenter<List<Item.ViewModel>> {
        fun onItemSelected(item: Item.ViewModel)
        fun onItemPressed(item: Item.ViewModel)

        fun onLinesVisibilityChanged(linesVisible: Boolean)
        fun onPlayPauseSelected()

        fun onGoToBookmarkSelected()
        fun onBookmarkPicked(cueId: Int)
        fun onAddBookmarkPicked(cueId: Int)
        fun onRemoveBookmarkPicked(cueId: Int)

        fun onAddNotePicked(cueId: Int)
        fun onShowNotePicked(cueId: Int)
        fun onEditNotePicked(cueId: Int)
        fun onRemoveNotesPicked(cueId: Int)
        fun onNoteEdited(cueId: Int, note: String)

        fun onEditCuePicked(cueId: Int)
        fun onCueEdited(cueId: Int, content: String, c: CharacterInfo)
        fun onDeleteCue(cueId: Int)
        fun onDeleteCueConfirmed(cueId: Int)
        fun onAddDialog(cueId: Int)
        fun onDialogWritten(cueId: Int, content: String, c: CharacterInfo)
        fun onAddAction(cueId: Int)
        fun onActionWritten(cueId: Int, content: String, c: CharacterInfo)
        fun onAddLyrics(cueId: Int)
        fun onLyricsWritten(cueId: Int, content: String, c: CharacterInfo)
    }

    interface View : ArchXView<List<Item.ViewModel>> {
        fun showLinesVisible(linesVisible: Boolean)

        fun showReading(reading: Boolean)
        fun scrollToRow(index: Int)

        fun showContextMenu(context: CueInfo)

        fun showBookmarksDialog(bookmarks: List<Pair<Int, String>>)
        fun showHasBookmarks(hasBookmarks: Boolean)

        fun showNotePrompt(cueId: Int, title: String, note: String)
        fun showNote(note: String)

        fun showEditCuePrompt(cueId: Int, content: String, characters: List<CharacterInfo>, selected: CharacterInfo?)
        fun showDeleteConfirm(cueId: Int, title: String)
        fun showAddActionPrompt(afterCueId: Int, characters: List<CharacterInfo>, selected: CharacterInfo?)
        fun showAddDialogPrompt(afterCueId: Int, characters: List<CharacterInfo>, selected: CharacterInfo?)
        fun showAddLyricsPrompt(afterCueId: Int, characters: List<CharacterInfo>, selected: CharacterInfo?)
    }

    interface DataSource : ArchXDataSource<List<CueWithCharacter>>

    interface CharacterDataSource : ArchXDataSource<List<CharacterModel>>

    interface DataSink : ArchXDataSink<List<CueWithCharacter>>

    interface Transformer
        : ArchXViewModelTransformer<List<CueWithCharacter>, List<Item.ViewModel>> {
        fun setUserLinesVisible(visible: Boolean)
        fun setSelectedCue(cueId: Int)
    }
}
