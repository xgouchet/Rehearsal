package fr.xgouchet.rehearsal.screen.home

import fr.xgouchet.rehearsal.core.model.Script
import fr.xgouchet.rehearsal.core.ui.PrincipledViewModelTransformer
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemEmpty
import fr.xgouchet.rehearsal.ui.ItemScript
import fr.xgouchet.rehearsal.ui.StableId

class HomeViewModelTransformer
    : PrincipledViewModelTransformer<Script, Item.ViewModel>(),
        HomeContract.Transformer {

    override fun empty(): Collection<Item.ViewModel> {
        return listOf(
                ItemEmpty.ViewModel(
                        id = StableId.getStableId(0, 0, Item.Type.EMPTY.ordinal),
                        title = "Welcome in Rehearsal",
                        body = "You can import a script using the + button below."
                )
        )
    }

    override fun transformItem(index: Int, item: Script): Collection<Item.ViewModel> {
        return listOf(ItemScript.ViewModel(
                id = StableId.getStableId(index, 0, Item.Type.SCRIPT.ordinal),
                title = item.title,
                author = item.author,
                data = item
        ))
    }
}
