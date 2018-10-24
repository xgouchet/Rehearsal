package fr.xgouchet.rehearsal.cast

import fr.xgouchet.archx.ArchXPresenter
import fr.xgouchet.archx.ArchXView
import fr.xgouchet.archx.ArchXViewModelTransformer
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.rehearsal.core.room.model.CharacterModel
import fr.xgouchet.rehearsal.ui.Item

interface CastContract {

    interface Presenter : ArchXPresenter<List<Item.ViewModel>> {
        fun onItemValueChanged(item: Any, value: String?)
    }

    interface View : ArchXView<List<Item.ViewModel>>

    interface DataSource : ArchXDataSource<List<CharacterModel>>

    interface DataSink {
        fun update(model: CharacterModel)
    }

    interface Transformer : ArchXViewModelTransformer<List<CharacterModel>, List<Item.ViewModel>>

}
