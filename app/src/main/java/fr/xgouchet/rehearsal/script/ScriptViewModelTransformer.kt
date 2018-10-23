package fr.xgouchet.rehearsal.script

import fr.xgouchet.rehearsal.core.room.model.SceneModel
import fr.xgouchet.rehearsal.core.ui.PrincipledViewModelTransformer
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemEmpty
import fr.xgouchet.rehearsal.ui.ItemScene

class ScriptViewModelTransformer
    : PrincipledViewModelTransformer<SceneModel, Item.ViewModel>(),
        ScriptContract.Transformer {

    override fun empty(): Collection<Item.ViewModel> {
        return listOf(
                ItemEmpty.ViewModel(
                        title = "¯\\_(ツ)_/¯",
                        body = "It seems this script is empty"
                )
        )
    }

    override fun items(item: SceneModel): Collection<Item.ViewModel> {
        return listOf(
                ItemScene.ViewModel(
                        title = item.description,
                        numbering = item.numbering,
                        data = item
                )
        )
    }

}
