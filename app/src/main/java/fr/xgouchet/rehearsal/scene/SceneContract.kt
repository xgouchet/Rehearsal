package fr.xgouchet.rehearsal.scene

import fr.xgouchet.archx.ArchXPresenter
import fr.xgouchet.archx.ArchXView
import fr.xgouchet.archx.ArchXViewModelTransformer
import fr.xgouchet.archx.data.ArchXDataSink
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.rehearsal.core.room.model.CueWithCharacter
import fr.xgouchet.rehearsal.ui.Item

interface SceneContract {

    interface Presenter : ArchXPresenter<List<Item.ViewModel>> {
        fun onItemSelected(item: Item.ViewModel)
        fun onLinesVisibilityChanged(linesVisible: Boolean)
        fun onPlayPauseSelected()
    }

    interface View : ArchXView<List<Item.ViewModel>> {
        fun showLinesVisible(linesVisible: Boolean)
        fun showReading(reading: Boolean)
        fun scrollToRow(index: Int)
    }

    interface DataSource : ArchXDataSource<List<CueWithCharacter>>

    interface DataSink : ArchXDataSink<List<CueWithCharacter>>

    interface Transformer
        : ArchXViewModelTransformer<List<CueWithCharacter>, List<Item.ViewModel>> {
        fun setUserLinesVisible(visible : Boolean)
        fun setSelectedCue(cueId: Int)
    }
}
