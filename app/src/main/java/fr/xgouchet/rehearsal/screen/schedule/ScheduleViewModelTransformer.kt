package fr.xgouchet.rehearsal.screen.schedule

import fr.xgouchet.rehearsal.core.model.Rehearsal
import fr.xgouchet.rehearsal.core.ui.PrincipledViewModelTransformer
import fr.xgouchet.rehearsal.ext.dayOfMonth
import fr.xgouchet.rehearsal.ext.month
import fr.xgouchet.rehearsal.ext.year
import fr.xgouchet.rehearsal.ui.DateFormatter
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemEmpty
import fr.xgouchet.rehearsal.ui.ItemRehearsal
import fr.xgouchet.rehearsal.ui.StableId
import java.util.Calendar

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
        val dueDate = Calendar.getInstance().apply { time = item.dueDate }
        val now = Calendar.getInstance().apply { timeInMillis = System.currentTimeMillis() }
        val time = when {
            now.year() == dueDate.year()
                    && now.month() == dueDate.month()
                    && now.dayOfMonth() == dueDate.dayOfMonth() -> ItemRehearsal.ViewModel.Time.PRESENT
            dueDate.before(now) -> ItemRehearsal.ViewModel.Time.PAST
            else -> ItemRehearsal.ViewModel.Time.FUTURE
        }
        return listOf(
                ItemRehearsal.ViewModel(
                        id = StableId.getStableId(index, 0, Item.Type.SCENE.ordinal),
                        title = DateFormatter.formatDate(item.dueDate),
                        time = time,
                        data = item
                )
        )
    }

}
