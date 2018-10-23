package fr.xgouchet.rehearsal.scene

import fr.xgouchet.archx.ArchXPresenter
import fr.xgouchet.archx.ArchXView
import fr.xgouchet.archx.ArchXViewModelTransformer
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.rehearsal.core.room.model.CharacterModel
import fr.xgouchet.rehearsal.core.room.model.CueWithCharacter
import fr.xgouchet.rehearsal.ui.Item

interface SceneContract {

    interface Presenter : ArchXPresenter<List<Item.ViewModel>> {
        fun onItemSelected(item: Any)
        fun onCastActionSelected()
    }

    interface View : ArchXView<List<Item.ViewModel>>

    interface CueDataSource : ArchXDataSource<List<CueWithCharacter>>
    interface CharacterDataSource : ArchXDataSource<List<CharacterModel>>

    interface Transformer : ArchXViewModelTransformer<List<CueWithCharacter>, List<Item.ViewModel>>
}
