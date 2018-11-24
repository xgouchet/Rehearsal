package fr.xgouchet.rehearsal.screen.script

import fr.xgouchet.rehearsal.core.model.Scene
import fr.xgouchet.rehearsal.core.ui.PrincipledViewModelTransformer
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemEmpty
import fr.xgouchet.rehearsal.ui.ItemScene
import fr.xgouchet.rehearsal.ui.StableId

class ScriptViewModelTransformer
    : PrincipledViewModelTransformer<Scene, Item.ViewModel>(),
        ScriptContract.Transformer {

    override fun empty(): Collection<Item.ViewModel> {
        return listOf(
                ItemEmpty.ViewModel(
                        id = StableId.getStableId(0, 0, Item.Type.EMPTY.ordinal),
                        title = "¯\\_(ツ)_/¯",
                        body = "It seems this script is empty"
                )
        )
    }

    override fun transformItem(index: Int, item: Scene): Collection<Item.ViewModel> {
        return listOf(
                ItemScene.ViewModel(
                        id = StableId.getStableId(item.position, 0, Item.Type.SCENE.ordinal),
                        title = item.title,
                        cuesCount = item.cuesCount,
                        data = item
                )
        )
    }

}
