package fr.xgouchet.rehearsal.home

import fr.xgouchet.archx.ArchXPresenter
import fr.xgouchet.archx.ArchXView
import fr.xgouchet.archx.ArchXViewModelTransformer
import fr.xgouchet.archx.data.ArchXDataSink
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.rehearsal.core.room.model.ScriptModel
import fr.xgouchet.rehearsal.ui.Item

interface HomeContract {

    interface Presenter : ArchXPresenter<List<Item.ViewModel>> {
        fun onItemSelected(item: Item.ViewModel)
    }

    interface View : ArchXView<List<Item.ViewModel>> {
        fun navigateToScript(script: ScriptModel)
    }

    interface DataSource : ArchXDataSource<List<ScriptModel>>

    interface DataSink : ArchXDataSink<List<ScriptModel>>

    interface Transformer : ArchXViewModelTransformer<List<ScriptModel>, List<Item.ViewModel>>

}
