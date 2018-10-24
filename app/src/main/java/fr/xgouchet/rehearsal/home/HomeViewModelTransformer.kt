package fr.xgouchet.rehearsal.home

import fr.xgouchet.rehearsal.core.room.model.ScriptModel
import fr.xgouchet.rehearsal.core.ui.PrincipledViewModelTransformer
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemEmpty
import fr.xgouchet.rehearsal.ui.ItemScript

class HomeViewModelTransformer
    : PrincipledViewModelTransformer<ScriptModel, Item.ViewModel>(),
        HomeContract.Transformer {

    override fun empty(): Collection<Item.ViewModel> {
        return listOf(
                ItemEmpty.ViewModel(
                        title = "Welcome in Rehearsal",
                        body = "You can import a script using the + button below."
                )
        )
    }

    override fun transformItem(index: Int, item: ScriptModel): Collection<Item.ViewModel> {
        return listOf(ItemScript.ViewModel(title = item.title, data = item))
    }
}
