package fr.xgouchet.rehearsal.schedule.list

import fr.xgouchet.archx.ArchXPresenter
import fr.xgouchet.archx.ArchXView
import fr.xgouchet.archx.ArchXViewModelTransformer
import fr.xgouchet.archx.data.ArchXDataSink
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.rehearsal.core.room.model.ScheduleModel
import fr.xgouchet.rehearsal.ui.Item

interface ScriptScheduleContract {

    interface Presenter : ArchXPresenter<List<Item.ViewModel>> {
        fun onItemSelected(item: Item.ViewModel)
        fun onAddSchedule()
    }

    interface View : ArchXView<List<Item.ViewModel>> {
        fun navigateToScheduleCreation(scriptId: Int, scriptTitle: String)
    }

    interface DataSource : ArchXDataSource<List<ScheduleModel>>

    interface DataSink : ArchXDataSink<List<ScheduleModel>>

    interface Transformer : ArchXViewModelTransformer<List<ScheduleModel>, List<Item.ViewModel>>
}
