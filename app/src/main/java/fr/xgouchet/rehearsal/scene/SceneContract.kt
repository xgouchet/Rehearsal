package fr.xgouchet.rehearsal.scene

import fr.xgouchet.archx.ArchXPresenter
import fr.xgouchet.archx.ArchXView
import fr.xgouchet.archx.ArchXViewModelTransformer
import fr.xgouchet.archx.data.ArchXDataSink
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.rehearsal.core.room.join.CueWithCharacter
import fr.xgouchet.rehearsal.ui.Item

interface SceneContract {

    interface Presenter : ArchXPresenter<List<Item.ViewModel>> {
        fun onItemSelected(item: Item.ViewModel)
        fun onItemPressed(item: Item.ViewModel)

        fun onLinesVisibilityChanged(linesVisible: Boolean)
        fun onPlayPauseSelected()

        fun onGoToBookmarkSelected()
        fun onBookmarkPicked(cueId: Int)

        fun onEditCuePicked(cueId: Int)
        fun onCueEdited(cueId: Int, content: String)

        fun onAddBookmarkPicked(cueId: Int)
        fun onRemoveBookmarkPicked(cueId: Int)

        fun onAddNotePicked(cueId: Int)
        fun onShowNotePicked(cueId: Int)
        fun onEditNotePicked(cueId: Int)
        fun onRemoveNotesPicked(cueId: Int)
        fun onNoteEdited(cueId: Int, note: String)
    }

    interface View : ArchXView<List<Item.ViewModel>> {
        fun showLinesVisible(linesVisible: Boolean)
        fun showReading(reading: Boolean)

        fun showEditCuePrompt(cueId: Int, content: String)

        fun showBookmarksDialog(bookmarks: List<Pair<Int, String>>)
        fun showHasBookmarks(hasBookmarks: Boolean)

        fun scrollToRow(index: Int)
        fun showContextMenu(context: CueInfo)

        fun showNotePrompt(cueId: Int, title: String, note: String)
        fun showNote(note: String)
    }

    interface DataSource : ArchXDataSource<List<CueWithCharacter>>

    interface DataSink : ArchXDataSink<List<CueWithCharacter>>

    interface Transformer
        : ArchXViewModelTransformer<List<CueWithCharacter>, List<Item.ViewModel>> {
        fun setUserLinesVisible(visible: Boolean)
        fun setSelectedCue(cueId: Int)
    }
}
