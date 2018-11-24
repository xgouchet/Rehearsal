package fr.xgouchet.rehearsal.screen.schedule

import fr.xgouchet.rehearsal.core.model.Rehearsal
import fr.xgouchet.rehearsal.core.ui.PrincipledViewModelTransformer
import fr.xgouchet.rehearsal.ui.DateFormatter
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemEmpty
import fr.xgouchet.rehearsal.ui.ItemRehearsal
import fr.xgouchet.rehearsal.ui.StableId

class ScheduleViewModelTransformer
    : PrincipledViewModelTransformer<Rehearsal, Item.ViewModel>(),
        ScheduleContract.Transformer {

    override fun empty(): Collection<Item.ViewModel> {
        return listOf(
                ItemEmpty.ViewModel(
                        id = StableId.getStableId(0, 0, Item.Type.EMPTY.ordinal),
                        title = "¯\\_(ツ)_/¯",
                        body = "You don't have any rehearsal goal scheduled."
                )
        )
    }

    override fun transformItem(index: Int, item: Rehearsal): Collection<Item.ViewModel> {
        val now = System.currentTimeMillis()
        val isPast = item.dueDate.time < now
        return listOf(
                ItemRehearsal.ViewModel(
                        id = StableId.getStableId(index, 0, Item.Type.SCENE.ordinal),
                        title = DateFormatter.formatDate(item.dueDate),
                        isPast = isPast,
                        data = item
                )
        )
    }

}
