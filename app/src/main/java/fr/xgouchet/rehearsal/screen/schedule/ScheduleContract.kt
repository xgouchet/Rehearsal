package fr.xgouchet.rehearsal.screen.schedule

import fr.xgouchet.archx.ArchXPresenter
import fr.xgouchet.archx.ArchXView
import fr.xgouchet.archx.transformer.ArchXViewModelTransformer
import fr.xgouchet.rehearsal.core.model.Rehearsal
import fr.xgouchet.rehearsal.ui.Item
import java.util.Date

interface ScheduleContract {

    interface Presenter : ArchXPresenter<List<Item.ViewModel>> {
        fun onItemSelected(item: Item.ViewModel)
        fun onAddSchedule()
        fun onDatePicked(date: Date)
    }

    interface View : ArchXView<List<Item.ViewModel>> {
        fun navigateToSetDetails(rehearsal: Rehearsal)
        fun showDatePicker(dueDate: Date)

        fun showError(throwable: Throwable)
    }


    interface Transformer : ArchXViewModelTransformer<List<Rehearsal>, List<Item.ViewModel>>
}
