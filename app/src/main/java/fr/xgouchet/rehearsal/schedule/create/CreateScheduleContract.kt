package fr.xgouchet.rehearsal.schedule.create

import fr.xgouchet.archx.ArchXPresenter
import fr.xgouchet.archx.ArchXView
import fr.xgouchet.archx.ArchXViewModelTransformer
import fr.xgouchet.archx.data.ArchXDataSink
import fr.xgouchet.rehearsal.core.room.model.RangeModel
import fr.xgouchet.rehearsal.core.room.model.ScheduleModel
import fr.xgouchet.rehearsal.ui.Item
import java.util.Date

interface CreateScheduleContract {

    interface Presenter : ArchXPresenter<List<Item.ViewModel>> {
        fun onItemValueChanged(item: Item.ViewModel, value: String?)
        fun onItemSelected(item: Item.ViewModel)

        fun onDatePicked(time: Date)
        fun onAddRangeClicked()
        fun onRangeCreated(range: RangeModel)
        fun onValidate()
    }

    interface View : ArchXView<List<Item.ViewModel>> {
        fun showDatePicker(dueDate: Date)
        fun showRangePicker(scriptId: Int)
        fun showValidate(valid: Boolean)
        fun showError(throwable: Throwable)
        fun navigateBack()
    }

    interface DataSink : ArchXDataSink<Pair<ScheduleModel, List<RangeModel>>>

    interface Transformer : ArchXViewModelTransformer<List<ScheduleModel>, List<Item.ViewModel>>
}
