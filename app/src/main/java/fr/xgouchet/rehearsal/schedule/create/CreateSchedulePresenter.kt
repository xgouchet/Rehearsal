package fr.xgouchet.rehearsal.schedule.create

import androidx.lifecycle.LifecycleOwner
import fr.xgouchet.archx.ArchXView
import fr.xgouchet.rehearsal.R
import fr.xgouchet.rehearsal.core.room.model.RangeModel
import fr.xgouchet.rehearsal.core.room.model.ScheduleModel
import fr.xgouchet.rehearsal.ui.DateFormatter
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemInteractive
import fr.xgouchet.rehearsal.ui.ItemRange
import fr.xgouchet.rehearsal.ui.StableId
import java.util.Date
import java.util.concurrent.TimeUnit

class CreateSchedulePresenter(
        private val scriptId: Int,
        private val owner: LifecycleOwner,
        private val dataSink: CreateScheduleContract.DataSink
) : CreateScheduleContract.Presenter {


    protected var view: CreateScheduleContract.View? = null

    private var dueDate: Date = Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7))

    private val ranges: MutableList<RangeModel> = mutableListOf()

    // region ArchXContract.Presenter

    override fun onViewAttached(view: ArchXView<List<Item.ViewModel>>, isRestored: Boolean) {
        this.view = view as? CreateScheduleContract.View
        this.view?.bindPresenter(this)

        updateForm()
    }

    override fun onViewDetached() {
        view = null
    }

    // endregion

    // region CreateScheduleContract.Presenter

    override fun onItemSelected(item: Item.ViewModel) {
        val subIndex = StableId.getSubIndex(item.getItemStableId())

        when (subIndex) {
            IDX_DUE_DATE -> {
                view?.showDatePicker(dueDate)
            }
        }
    }

    override fun onItemValueChanged(item: Item.ViewModel, value: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDatePicked(time: Date) {
        dueDate = time
        updateForm()
    }

    override fun onAddRangeClicked() {
        view?.showRangePicker(scriptId)
    }

    override fun onRangeCreated(range: RangeModel) {
        ranges.add(range)
        updateForm()
    }

    override fun onValidate() {
        val schedule = ScheduleModel(
                scheduleId = 0,
                note = null,
                scriptId = scriptId,
                dueDate = dueDate
        )

        dataSink.createData(schedule to ranges) {
            if (it == null) {
                view?.navigateBack()
            } else {
                view?.showError(it)
            }
        }
    }

    // endregion


    // region Internal


    private fun updateForm() {
        view?.showData(prepareForm())

        val isValid = ranges.isNotEmpty()
        view?.showValidate(isValid)
    }

    private fun prepareForm(): List<Item.ViewModel> {
        val list = mutableListOf<Item.ViewModel>()

        list.add(ItemInteractive.ViewModel(
                id = StableId.getStableId(0, IDX_DUE_DATE, Item.Type.INTERACTIVE.ordinal),
                labelRes = R.string.schedule_action_dueDate,
                value = DateFormatter.formatDate(dueDate)
        ))

        ranges.forEachIndexed { i, range ->
            list.add(
                    ItemRange.ViewModel(
                            id = StableId.getStableId(i, IDX_RANGE, Item.Type.RANGE.ordinal),
                            scene = range.scene?.description.orEmpty(),
                            startLine = range.startCue?.content.orEmpty(),
                            endLine = range.endCue?.content.orEmpty(),
                            data = range
                    )
            )
        }

        return list
    }

    // endregion

    companion object {

        const val IDX_DUE_DATE = 1
        const val IDX_RANGE = 2
        const val IDX_ENGINE = 4
        const val IDX_PITCH = 5
        const val IDX_RATE = 6
    }
}
