package fr.xgouchet.rehearsal.script

import fr.xgouchet.rehearsal.core.room.model.SceneModel
import fr.xgouchet.rehearsal.core.ui.PrincipledViewModelTransformer
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemEmpty
import fr.xgouchet.rehearsal.ui.ItemScene
import fr.xgouchet.rehearsal.ui.StableId

class ScriptViewModelTransformer
    : PrincipledViewModelTransformer<SceneModel, Item.ViewModel>(),
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

    override fun transformItem(index: Int, item: SceneModel): Collection<Item.ViewModel> {
        return listOf(
                ItemScene.ViewModel(
                        id = StableId.getStableId(index, 0, Item.Type.SCENE.ordinal),
                        title = item.description,
                        numbering = item.numbering,
                        data = item
                )
        )
    }

}
