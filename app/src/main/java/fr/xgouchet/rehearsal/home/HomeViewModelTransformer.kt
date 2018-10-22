package fr.xgouchet.rehearsal.home

import fr.xgouchet.rehearsal.core.room.model.ScriptModel
import fr.xgouchet.rehearsal.core.ui.PrincipledViewModelTransformer
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemEmpty
import fr.xgouchet.rehearsal.ui.ItemHeader
import fr.xgouchet.rehearsal.ui.ItemScript

class HomeViewModelTransformer
    : PrincipledViewModelTransformer<ScriptModel, Item.ViewModel>() {

    override fun empty(): Collection<Item.ViewModel> {
        return listOf(ItemEmpty.ViewModel("Welcome in Rehearsal", "You can import a script using the + button below."))
    }

    override fun headers(appModel: List<ScriptModel>): Collection<Item.ViewModel> {
        return listOf(ItemHeader.ViewModel("Scripts"))
    }

    override fun items(item: ScriptModel): Collection<Item.ViewModel> {
        return listOf(ItemScript.ViewModel(item.title))
    }
}
