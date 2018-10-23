package fr.xgouchet.rehearsal.scene

import fr.xgouchet.archx.ArchXPresenter
import fr.xgouchet.archx.ArchXView
import fr.xgouchet.archx.ArchXViewModelTransformer
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.rehearsal.core.room.model.CueWithCharacter
import fr.xgouchet.rehearsal.ui.Item

interface SceneContract {

    interface Presenter : ArchXPresenter<List<Item.ViewModel>> {
        fun onItemSelected(item: Any)
    }

    interface View : ArchXView<List<Item.ViewModel>>

    interface DataSource : ArchXDataSource<List<CueWithCharacter>>

    interface Transformer : ArchXViewModelTransformer<List<CueWithCharacter>, List<Item.ViewModel>>
}
