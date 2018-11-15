package fr.xgouchet.rehearsal.search.cue

import fr.xgouchet.archx.ArchXPresenter
import fr.xgouchet.archx.ArchXView
import fr.xgouchet.archx.ArchXViewModelTransformer
import fr.xgouchet.archx.data.ArchXDataSink
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.rehearsal.core.room.model.ScheduleModel
import fr.xgouchet.rehearsal.ui.Item

interface SearchCueContract {

    interface Presenter : ArchXPresenter<List<Item.ViewModel>>

    interface View : ArchXView<List<Item.ViewModel>>

    interface DataSource : ArchXDataSource<List<ScheduleModel>>

    interface DataSink : ArchXDataSink<List<ScheduleModel>>

    interface Transformer : ArchXViewModelTransformer<List<ScheduleModel>, List<Item.ViewModel>>
}
