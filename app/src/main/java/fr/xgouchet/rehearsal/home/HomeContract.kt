package fr.xgouchet.rehearsal.home

import fr.xgouchet.archx.ArchXPresenter
import fr.xgouchet.archx.ArchXView
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.rehearsal.core.room.model.ScriptModel
import fr.xgouchet.rehearsal.ui.Item

interface HomeContract {

    interface Presenter : ArchXPresenter<List<Item.ViewModel>> {
        fun onItemSelected(item: Any)
    }

    interface View : ArchXView<List<Item.ViewModel>>

    interface DataSource : ArchXDataSource<List<ScriptModel>>


}
