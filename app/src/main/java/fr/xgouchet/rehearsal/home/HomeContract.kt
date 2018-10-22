package fr.xgouchet.rehearsal.home

import fr.xgouchet.archx.ArchXPresenter
import fr.xgouchet.archx.ArchXView
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.rehearsal.core.room.ScriptModel
import fr.xgouchet.rehearsal.core.ui.script.ScriptViewModel

interface HomeContract {

    interface Presenter : ArchXPresenter<List<ScriptViewModel>>

    interface View : ArchXView<List<ScriptViewModel>>

    interface DataSource : ArchXDataSource<List<ScriptModel>>


}