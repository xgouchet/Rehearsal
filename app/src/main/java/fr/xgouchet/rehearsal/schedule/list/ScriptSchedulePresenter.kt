package fr.xgouchet.rehearsal.schedule.list

import androidx.lifecycle.LifecycleOwner
import fr.xgouchet.archx.data.ArchXDataPresenter
import fr.xgouchet.rehearsal.core.room.model.ScheduleModel
import fr.xgouchet.rehearsal.ui.Item

class ScriptSchedulePresenter(
        private val scriptId: Int,
        private val scriptTitle: String,
        owner: LifecycleOwner,
        dataSource: ScriptScheduleContract.DataSource,
        dataSink: ScriptScheduleContract.DataSink,
        transformer: ScriptScheduleContract.Transformer
) : ArchXDataPresenter<List<ScheduleModel>, ScriptScheduleContract.View, List<Item.ViewModel>>(owner, dataSource, dataSink, transformer),
        ScriptScheduleContract.Presenter {

    // region ScriptScheduleContract.Presenter

    override fun onItemSelected(item: Item.ViewModel) {
       // TODO()
    }

    override fun onAddSchedule() {
        view?.navigateToScheduleCreation(scriptId, scriptTitle)
    }
    // endregion

}
