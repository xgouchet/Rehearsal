package fr.xgouchet.rehearsal.schedule.details

import fr.xgouchet.rehearsal.core.room.model.RangeModel
import fr.xgouchet.rehearsal.core.ui.PrincipledViewModelTransformer
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemEmpty
import fr.xgouchet.rehearsal.ui.ItemRange
import fr.xgouchet.rehearsal.ui.StableId

class ScheduleRangesViewModelTransformer
    : PrincipledViewModelTransformer<RangeModel, Item.ViewModel>(),
        ScheduleRangesContract.Transformer {

    override fun empty(): Collection<Item.ViewModel> {
        return listOf(
                ItemEmpty.ViewModel(
                        id = StableId.getStableId(0, 0, Item.Type.EMPTY.ordinal),
                        title = "¯\\_(ツ)_/¯",
                        body = "It seems this script is empty"
                )
        )
    }

    override fun transformItem(index: Int, item: RangeModel): Collection<Item.ViewModel> {
        return listOf(
                ItemRange.ViewModel(
                        id = StableId.getStableId(index, 0, Item.Type.RANGE.ordinal),
                        scene = item.scene?.description.orEmpty(),
                        startLine = item.startCue?.content.orEmpty(),
                        endLine = item.endCue?.content.orEmpty(),
                        data = item
                )
        )
    }

}
