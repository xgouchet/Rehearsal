package fr.xgouchet.rehearsal.screen.rehearsal

import fr.xgouchet.rehearsal.core.model.Range
import fr.xgouchet.rehearsal.core.ui.PrincipledViewModelTransformer
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemEmpty
import fr.xgouchet.rehearsal.ui.ItemRange
import fr.xgouchet.rehearsal.ui.StableId

class RehearsalViewModelTransformer
    : PrincipledViewModelTransformer<Range, Item.ViewModel>(),
        RehearsalContract.Transformer {

    override fun empty(): Collection<Item.ViewModel> {
        return listOf(
                ItemEmpty.ViewModel(
                        id = StableId.getStableId(0, 0, Item.Type.EMPTY.ordinal),
                        title = "¯\\_(ツ)_/¯",
                        body = "It seems this script is empty"
                )
        )
    }

    override fun transformItem(index: Int, item: Range): Collection<Item.ViewModel> {
        return listOf(
                ItemRange.ViewModel(
                        id = StableId.getStableId(index, 0, Item.Type.RANGE.ordinal),
                        scene = item.scene.title,
                        startCharacter = item.fromCue.character?.name.orEmpty(),
                        startLine = item.fromCue.content,
                        endCharacter = item.toCue.character?.name.orEmpty(),
                        endLine = item.toCue.content,
                        data = item
                )
        )
    }

}
