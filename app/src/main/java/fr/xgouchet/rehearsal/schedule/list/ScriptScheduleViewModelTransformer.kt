package fr.xgouchet.rehearsal.schedule.list

import fr.xgouchet.rehearsal.core.room.model.ScheduleModel
import fr.xgouchet.rehearsal.core.ui.PrincipledViewModelTransformer
import fr.xgouchet.rehearsal.ui.DateFormatter
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemEmpty
import fr.xgouchet.rehearsal.ui.ItemSchedule
import fr.xgouchet.rehearsal.ui.StableId

class ScriptScheduleViewModelTransformer
    : PrincipledViewModelTransformer<ScheduleModel, Item.ViewModel>(),
        ScriptScheduleContract.Transformer {

    override fun empty(): Collection<Item.ViewModel> {
        return listOf(
                ItemEmpty.ViewModel(
                        id = StableId.getStableId(0, 0, Item.Type.EMPTY.ordinal),
                        title = "¯\\_(ツ)_/¯",
                        body = "You don't have any rehearsal goal scheduled."
                )
        )
    }

    override fun transformItem(index: Int, item: ScheduleModel): Collection<Item.ViewModel> {
        val now = System.currentTimeMillis()
        val isPast = item.dueDate.time < now
        return listOf(
                ItemSchedule.ViewModel(
                        id = StableId.getStableId(index, 0, Item.Type.SCENE.ordinal),
                        title = DateFormatter.formatDate(item.dueDate),
                        isPast = isPast,
                        data = item
                )
        )
    }

}
